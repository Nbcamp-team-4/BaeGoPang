package com.team.project.domain.order.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.team.project.domain.order.model.dto.GetOrdersCommand;
import com.team.project.domain.order.model.dto.GetOrdersQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.team.project.domain.order.api.request.CancelOrderRequest;
import com.team.project.domain.order.api.request.ConfirmOrderPaymentRequest;
import com.team.project.domain.order.api.request.CreateOrderRequest;
import com.team.project.domain.order.api.request.UpdateOrderStatusRequest;
import com.team.project.domain.order.api.response.CancelOrderResponse;
import com.team.project.domain.order.api.response.ConfirmOrderPaymentResponse;
import com.team.project.domain.order.api.response.CreateOrderResponse;
import com.team.project.domain.order.api.response.GetOrderDetailResponse;
import com.team.project.domain.order.api.response.GetOrderSummaryResponse;
import com.team.project.domain.order.api.response.UpdateOrderStatusResponse;
import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.entity.OrderItem;
import com.team.project.domain.order.entity.OrderItemOption;
import com.team.project.domain.order.exception.InvalidOrderStatusException;
import com.team.project.domain.order.exception.OrderAlreadyCanceledException;
import com.team.project.domain.order.exception.OrderCannotCancelException;
import com.team.project.domain.order.exception.OrderForbiddenException;
import com.team.project.domain.order.exception.OrderNotFoundException;
import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.domain.order.repository.OrderRepository;
import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.dto.CancelPaymentQuery;
import com.team.project.domain.payment.model.dto.CreatePaymentCommand;
import com.team.project.domain.payment.model.dto.PayPaymentCommand;
import com.team.project.domain.payment.model.dto.PayPaymentQuery;
import com.team.project.domain.payment.service.PaymentService;
import com.team.project.domain.product.entity.Product;
import com.team.project.domain.product.repository.ProductRepository;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.repository.StoreRepository;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserAddress;
import com.team.project.domain.user.repository.UserAddressRepository;
import com.team.project.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final ProductRepository productRepository;
	private final UserAddressRepository userAddressRepository;

	private final PaymentService paymentService;
	private final OrderPaymentProcessor orderPaymentProcessor;

	/**
	 * 로그인한 OWNER가 실제 해당 가게 주인인지 확인
	 */
	private Store getOwnedStore(UUID ownerUserId, UUID storeId) {
		return storeRepository.findByIdAndUserId(storeId, ownerUserId)
				.orElseThrow(OrderForbiddenException::new);
	}

	@Override
	@Transactional
	public CreateOrderResponse createOrder(UUID userId, CreateOrderRequest request) {

		// 로그인 사용자 기준으로 유저 조회
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

		// 가게 존재 확인
		Store store = storeRepository.findById(request.getStoreId())
				.orElseThrow(() -> new IllegalArgumentException("STORE_NOT_FOUND"));

		// 주문번호 생성
		String orderNo = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

		// 총 주문 금액 계산
		int totalAmount = request.getItems().stream()
				.mapToInt(item -> {
					int optionSum = 0;
					if (item.getOptions() != null) {
						optionSum = item.getOptions().stream()
								.mapToInt(opt -> opt.getExtraPrice() == null ? 0 : opt.getExtraPrice())
								.sum();
					}
					return (item.getUnitPrice() + optionSum) * item.getQuantity();
				})
				.sum();

		// 로그인 사용자 본인 배송지만 허용
		UserAddress deliveryAddress = null;
		if (request.getDeliveryAddressId() != null) {
			deliveryAddress = userAddressRepository
					.findByIdAndUserId(request.getDeliveryAddressId(), userId)
					.orElseThrow(() -> new IllegalArgumentException("DELIVERY_ADDRESS_NOT_FOUND"));
		}

		Order order = new Order(
				user,
				store,
				deliveryAddress,
				orderNo,
				totalAmount,
				request.getRequestMemo()
		);

		for (CreateOrderRequest.CreateOrderItemRequest itemReq : request.getItems()) {
			Product product = productRepository.findById(itemReq.getProductId())
					.orElseThrow(() -> new IllegalArgumentException("PRODUCT_NOT_FOUND"));

			OrderItem orderItem = new OrderItem(
					product,
					itemReq.getProductName(),
					itemReq.getUnitPrice(),
					itemReq.getQuantity()
			);

			if (itemReq.getOptions() != null) {
				for (CreateOrderRequest.CreateOrderItemOptionRequest optReq : itemReq.getOptions()) {
					OrderItemOption option = new OrderItemOption(
							optReq.getOptionName(),
							optReq.getOptionItemName(),
							optReq.getExtraPrice()
					);
					orderItem.addOption(option);
				}
			}

			order.addItem(orderItem);
		}

		Order savedOrder = orderRepository.save(order);

		// 주문 생성 후 결제 엔티티 생성
		paymentService.createPayment(
				CreatePaymentCommand.of(savedOrder, savedOrder.getTotalAmount())
		);

		return CreateOrderResponse.from(savedOrder);
	}

	@Override
	@Transactional
	public ConfirmOrderPaymentResponse confirmOrderPayment(UUID userId, UUID orderId, ConfirmOrderPaymentRequest request) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(OrderNotFoundException::new);

		// 로그인한 사용자 본인 주문인지 확인
		if (!order.getUser().getId().equals(userId)) {
			throw new OrderForbiddenException();
		}

		// 결제 대기 상태인지 확인
		if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
			throw new InvalidOrderStatusException();
		}

		// 주문 금액 검증
		if (!order.getTotalAmount().equals(request.getAmount())) {
			throw new IllegalArgumentException("주문 금액과 결제 승인 금액이 일치하지 않습니다.");
		}

		PayPaymentQuery payPaymentQuery = paymentService.payPayment(
				PayPaymentCommand.of(orderId, request.getPaymentKey(), request.getAmount())
		);

		order.markPaid();

		return ConfirmOrderPaymentResponse.from(order, payPaymentQuery);
	}

	@Override
	public GetOrderDetailResponse getOrderDetail(UUID orderId, UUID userId) {

		// 로그인 사용자 기준 주문 상세 조회
		Order order = orderRepository.findDetailByIdAndUserId(orderId, userId)
				.orElseThrow(OrderNotFoundException::new);

		Payment payment = orderPaymentProcessor.getLatestPaymentOrNull(order.getId());

		return GetOrderDetailResponse.from(order, payment);
	}

	@Override
	public GetOrdersQuery getMyOrders(UUID userId, GetOrdersCommand command) {

		Pageable pageable = PageRequest.of(command.getPage(), command.getSize());

		Page<Order> orderPage = orderRepository.searchMyOrders(
				userId,
				command.getStatus(),
				command.getRangeCreatedAt(),
				pageable
		);

		List<GetOrdersQuery.Item> items = orderPage.getContent()
				.stream()
				.map(order -> GetOrdersQuery.Item.builder()
						.id(order.getId())
						.orderNo(order.getOrderNo())
						.status(order.getStatus())
						.totalAmount(order.getTotalAmount())
						.createdAt(order.getCreatedAt())
						.storeId(order.getStore().getId())
						.storeName(order.getStore().getName())
						.userId(order.getUser().getId())
						.userName(order.getUser().getName())
						.build())
				.toList();

		return GetOrdersQuery.builder()
				.content(items)
				.page(orderPage.getNumber())
				.size(orderPage.getSize())
				.totalElements(orderPage.getTotalElements())
				.totalPages(orderPage.getTotalPages())
				.build();
	}

	@Override
	@Transactional
	public CancelOrderResponse cancelOrder(UUID orderId, UUID userId, CancelOrderRequest request) {

		Order order = orderRepository.findDetailById(orderId)
				.orElseThrow(OrderNotFoundException::new);

		// 본인 주문인지 확인
		if (!order.getUser().getId().equals(userId)) {
			throw new OrderForbiddenException();
		}

		// 이미 취소된 주문인지 확인
		if (order.getStatus() == OrderStatus.CANCELED) {
			throw new OrderAlreadyCanceledException();
		}

		// 완료된 주문은 취소 불가
		if (order.getStatus() == OrderStatus.COMPLETED) {
			throw new OrderCannotCancelException();
		}

		// 현재 정책상 취소 가능한 주문 상태인지 확인
		if (order.getStatus() != OrderStatus.PENDING_PAYMENT
				&& order.getStatus() != OrderStatus.PAID
				&& order.getStatus() != OrderStatus.ACCEPTED) {
			throw new InvalidOrderStatusException();
		}

		// 취소 사유 필수 검증
		if (request == null || !StringUtils.hasText(request.getReason())) {
			throw new IllegalArgumentException("주문 취소 사유는 필수입니다.");
		}

		// 결제 취소 처리
		CancelPaymentQuery cancelPaymentQuery =
				orderPaymentProcessor.cancelForOrder(order.getId(), request.getReason());

		// 주문 상태 취소 처리
		order.cancel(request.getReason());

		return CancelOrderResponse.from(order, cancelPaymentQuery.getId(), cancelPaymentQuery.getStatus());
	}

	@Override
	@Transactional
	public void deleteOrder(UUID orderId, UUID userId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(OrderNotFoundException::new);

		// 본인 주문인지 확인
		if (!order.getUser().getId().equals(userId)) {
			throw new OrderForbiddenException();
		}

		// 소프트 삭제 처리
		order.markDeleted(userId);
	}

	@Override
	public GetOrdersQuery getStoreOrders(UUID ownerUserId, UUID storeId, GetOrdersCommand command) {

		// 로그인한 OWNER의 가게인지 확인
		getOwnedStore(ownerUserId, storeId);

		Pageable pageable = PageRequest.of(command.getPage(), command.getSize());

		Page<Order> orderPage = orderRepository.searchStoreOrders(
				storeId,
				command.getStatus(),
				command.getRangeCreatedAt(),
				pageable
		);

		List<GetOrdersQuery.Item> items = orderPage.getContent()
				.stream()
				.map(order -> GetOrdersQuery.Item.builder()
						.id(order.getId())
						.orderNo(order.getOrderNo())
						.status(order.getStatus())
						.totalAmount(order.getTotalAmount())
						.createdAt(order.getCreatedAt())
						.storeId(order.getStore().getId())
						.storeName(order.getStore().getName())
						.userId(order.getUser().getId())
						.userName(order.getUser().getName())
						.build())
				.toList();

		return GetOrdersQuery.builder()
				.content(items)
				.page(orderPage.getNumber())
				.size(orderPage.getSize())
				.totalElements(orderPage.getTotalElements())
				.totalPages(orderPage.getTotalPages())
				.build();
	}

	@Override
	public GetOrderDetailResponse getStoreOrderDetail(UUID ownerUserId, UUID orderId, UUID storeId) {

		// 로그인한 OWNER의 가게인지 확인
		getOwnedStore(ownerUserId, storeId);

		Order order = orderRepository.findDetailByIdAndStoreId(orderId, storeId)
				.orElseThrow(OrderNotFoundException::new);

		Payment payment = orderPaymentProcessor.getLatestPaymentOrNull(order.getId());

		return GetOrderDetailResponse.from(order, payment);
	}

	@Override
	@Transactional
	public UpdateOrderStatusResponse acceptOrder(UUID ownerUserId, UUID orderId, UUID storeId) {

		// 로그인한 OWNER의 가게인지 확인
		getOwnedStore(ownerUserId, storeId);

		Order order = orderRepository.findById(orderId)
				.orElseThrow(OrderNotFoundException::new);

		// 해당 가게 주문인지 확인
		if (!order.getStore().getId().equals(storeId)) {
			throw new OrderForbiddenException();
		}

		// 결제 완료 상태의 주문만 수락 가능
		if (order.getStatus() != OrderStatus.PAID) {
			throw new InvalidOrderStatusException();
		}

		order.accept();

		Payment payment = orderPaymentProcessor.getLatestPaymentOrNull(order.getId());
		return UpdateOrderStatusResponse.from(order, payment);
	}

	@Override
	@Transactional
	public UpdateOrderStatusResponse rejectOrder(UUID ownerUserId, UUID orderId, UUID storeId, String reason) {

		// 로그인한 OWNER의 가게인지 확인
		getOwnedStore(ownerUserId, storeId);

		Order order = orderRepository.findById(orderId)
				.orElseThrow(OrderNotFoundException::new);

		// 해당 가게 주문인지 확인
		if (!order.getStore().getId().equals(storeId)) {
			throw new OrderForbiddenException();
		}

		// 결제 완료 상태의 주문만 거절 가능
		if (order.getStatus() != OrderStatus.PAID) {
			throw new InvalidOrderStatusException();
		}

		// 거절 사유 필수 검증
		if (!StringUtils.hasText(reason)) {
			throw new IllegalArgumentException("주문 거절 사유는 필수입니다.");
		}

		CancelPaymentQuery cancelPaymentQuery =
				orderPaymentProcessor.refundForRejectedOrder(order.getId(), reason);

		order.reject(reason);

		return UpdateOrderStatusResponse.from(order, cancelPaymentQuery.getId(), cancelPaymentQuery.getStatus());
	}

	@Override
	@Transactional
	public UpdateOrderStatusResponse updateOrderStatusByStore(UUID ownerUserId, UUID orderId, UUID storeId,
															  UpdateOrderStatusRequest request) {

		// 로그인한 OWNER의 가게인지 확인
		getOwnedStore(ownerUserId, storeId);

		Order order = orderRepository.findById(orderId)
				.orElseThrow(OrderNotFoundException::new);

		// 해당 가게 주문인지 확인
		if (!order.getStore().getId().equals(storeId)) {
			throw new OrderForbiddenException();
		}

		OrderStatus target = request.getStatus();

		if (target == OrderStatus.ACCEPTED) {
			order.accept();

		} else if (target == OrderStatus.COOKING) {
			order.startCooking();

		} else if (target == OrderStatus.DELIVERING) {
			order.startDelivering();

		} else if (target == OrderStatus.COMPLETED) {
			order.complete();

		} else if (target == OrderStatus.REJECTED) {
			String reason = "STORE_STATUS_UPDATE";

			CancelPaymentQuery cancelPaymentQuery =
					orderPaymentProcessor.refundForRejectedOrder(order.getId(), reason);

			order.reject(reason);

			return UpdateOrderStatusResponse.from(
					order,
					cancelPaymentQuery.getId(),
					cancelPaymentQuery.getStatus()
			);

		} else {
			throw new InvalidOrderStatusException();
		}

		Payment updatedPayment = orderPaymentProcessor.getLatestPaymentOrNull(order.getId());
		return UpdateOrderStatusResponse.from(order, updatedPayment);
	}
}