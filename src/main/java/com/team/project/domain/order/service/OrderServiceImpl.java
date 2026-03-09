package com.team.project.domain.order.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.team.project.domain.payment.model.dto.CreatePaymentQuery;
import com.team.project.domain.payment.model.dto.PayPaymentCommand;
import com.team.project.domain.payment.model.dto.PayPaymentQuery;
import com.team.project.domain.payment.service.PaymentService;
import com.team.project.domain.product.entity.Product;
import com.team.project.domain.product.repository.ProductRepository;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.repository.StoreRepository;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.address.entity.UserAddress;
import com.team.project.domain.address.repository.UserAddressRepository;
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

	@Override
	@Transactional
	public CreateOrderResponse createOrder(CreateOrderRequest request) {

		// 1. 유저 존재 확인
		User user = userRepository.findById(request.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

		// 2. 가게 존재 확인
		Store store = storeRepository.findById(request.getStoreId())
			.orElseThrow(() -> new IllegalArgumentException("STORE_NOT_FOUND"));

		// 3. 주문번호 생성
		String orderNo = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

		// 4. 총 주문 금액 계산
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

		// 5. 배송지 조회
		UserAddress deliveryAddress = null;
		if (request.getDeliveryAddressId() != null) {
			deliveryAddress = userAddressRepository
				.findByIdAndUserId(request.getDeliveryAddressId(), request.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("DELIVERY_ADDRESS_NOT_FOUND"));
		}

		// 6. 주문 엔티티 생성
		Order order = new Order(
			user,
			store,
			deliveryAddress,
			orderNo,
			totalAmount,
			request.getRequestMemo()
		);

		// 7. 주문 아이템 / 옵션 생성 및 주문에 연결
		for (CreateOrderRequest.CreateOrderItemRequest itemReq : request.getItems()) {
			Product product = productRepository.findById(itemReq.getProductId())
				.orElseThrow(() -> new IllegalArgumentException("PRODUCT_NOT_FOUND"));

			/*
			 * TODO
			 * Product 엔티티에 store 연관관계가 있다면 아래 검증을 활성화하는 것이 좋습니다.
			 *
			 * if (!product.getStore().getId().equals(store.getId())) {
			 *     throw new IllegalArgumentException("해당 가게의 상품만 주문할 수 있습니다.");
			 * }
			 */

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

		// 8. 주문 저장
		Order savedOrder = orderRepository.save(order);

		// 8) 결제 생성
		CreatePaymentQuery paymentQuery = paymentService.createPayment(
			CreatePaymentCommand.of(savedOrder, savedOrder.getTotalAmount())); // 응답에 사용하시면 payment 상태까지 같이 보낼수 있습니다.

		// 10. 응답 반환
		return CreateOrderResponse.from(savedOrder);
	}

	@Override
	@Transactional
	public ConfirmOrderPaymentResponse confirmOrderPayment(UUID orderId, ConfirmOrderPaymentRequest request) {

		// 1. 주문 조회
		Order order = orderRepository.findById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		// 2. 결제 대기 상태인지 확인
		if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
			throw new InvalidOrderStatusException();
		}

		// 3. 주문 총 금액과 결제 승인 요청 금액이 일치하는지 검증
		if (!order.getTotalAmount().equals(request.getAmount())) {
			throw new IllegalArgumentException("주문 금액과 결제 승인 금액이 일치하지 않습니다.");
		}

		// 4. 결제 승인 처리
		PayPaymentQuery payPaymentQuery = paymentService.payPayment(
			PayPaymentCommand.of(orderId, request.getPaymentKey(), request.getAmount())
		);

		// 5. 주문 상태를 결제 완료로 변경
		order.markPaid();

		// 6. 응답 반환
		return ConfirmOrderPaymentResponse.from(order, payPaymentQuery);
	}

	@Override
	public GetOrderDetailResponse getOrderDetail(UUID orderId, UUID userId) {

		// 1. 사용자 기준 주문 상세 조회
		Order order = orderRepository.findDetailByIdAndUserId(orderId, userId)
			.orElseThrow(OrderNotFoundException::new);

		// 2. 최신 결제 정보 조회
		Payment payment = orderPaymentProcessor.getLatestPaymentOrNull(order.getId());

		// 3. 응답 반환
		return GetOrderDetailResponse.from(order, payment);
	}

	@Override
	public List<GetOrderSummaryResponse> getMyOrders(UUID userId) {

		// 1. 사용자 주문 목록 조회
		List<Order> orders = orderRepository.findAllByUserIdOrderByOrderDateDesc(userId);

		// 2. 각 주문별 최신 결제 정보와 함께 응답 변환
		return orders.stream()
			.map(order -> {
				Payment payment = orderPaymentProcessor.getLatestPaymentOrNull(order.getId());
				return GetOrderSummaryResponse.from(order, payment);
			})
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public CancelOrderResponse cancelOrder(UUID orderId, UUID userId, CancelOrderRequest request) {

		// 1. 주문 상세 조회
		Order order = orderRepository.findDetailById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		// 2. 본인 주문인지 확인
		if (!order.getUser().getId().equals(userId)) {
			throw new OrderForbiddenException();
		}

		// 3. 이미 취소된 주문인지 확인
		if (order.getStatus() == OrderStatus.CANCELED) {
			throw new OrderAlreadyCanceledException();
		}

		// 4. 완료된 주문은 취소 불가
		if (order.getStatus() == OrderStatus.COMPLETED) {
			throw new OrderCannotCancelException();
		}

		// 5. 현재 정책상 취소 가능한 주문 상태인지 확인
		if (order.getStatus() != OrderStatus.PENDING_PAYMENT
			&& order.getStatus() != OrderStatus.PAID
			&& order.getStatus() != OrderStatus.ACCEPTED) {
			throw new InvalidOrderStatusException();
		}

		// 6. 취소 사유 필수 검증
		if (request == null || !StringUtils.hasText(request.getReason())) {
			throw new IllegalArgumentException("주문 취소 사유는 필수입니다.");
		}

		// 7. 결제 취소 처리
		// 실제 결제 취소 / 환불 연동 책임은 OrderPaymentProcessor에 위임
		CancelPaymentQuery cancelPaymentQuery =
			orderPaymentProcessor.cancelForOrder(order.getId(), request.getReason());

		// 8. 주문 상태 취소 처리
		order.cancel(request.getReason());

		// 9. 응답 반환
		return CancelOrderResponse.from(order, cancelPaymentQuery.getId(), cancelPaymentQuery.getStatus());
	}

	@Override
	@Transactional
	public void deleteOrder(UUID orderId, UUID userId) {

		// 1. 주문 조회
		Order order = orderRepository.findById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		// 2. 본인 주문인지 확인
		if (!order.getUser().getId().equals(userId)) {
			throw new OrderForbiddenException();
		}

		// 3. 소프트 삭제 처리
		order.markDeleted(userId);
	}

	@Override
	public List<GetOrderSummaryResponse> getStoreOrders(UUID storeId) {

		// 1. 가게 주문 목록 조회
		List<Order> orders = orderRepository.findAllByStoreIdOrderByOrderDateDesc(storeId);

		// 2. 각 주문별 최신 결제 정보와 함께 응답 변환
		return orders.stream()
			.map(order -> {
				Payment payment = orderPaymentProcessor.getLatestPaymentOrNull(order.getId());
				return GetOrderSummaryResponse.from(order, payment);
			})
			.collect(Collectors.toList());
	}

	@Override
	public GetOrderDetailResponse getStoreOrderDetail(UUID orderId, UUID storeId) {

		// 1. 가게 기준 주문 상세 조회
		Order order = orderRepository.findDetailByIdAndStoreId(orderId, storeId)
			.orElseThrow(OrderNotFoundException::new);

		// 2. 최신 결제 정보 조회
		Payment payment = orderPaymentProcessor.getLatestPaymentOrNull(order.getId());

		// 3. 응답 반환
		return GetOrderDetailResponse.from(order, payment);
	}

	@Override
	@Transactional
	public UpdateOrderStatusResponse acceptOrder(UUID orderId, UUID storeId) {

		// 1. 주문 조회
		Order order = orderRepository.findById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		// 2. 해당 가게 주문인지 확인
		if (!order.getStore().getId().equals(storeId)) {
			throw new OrderForbiddenException();
		}

		// 3. 결제 완료 상태의 주문만 수락 가능
		if (order.getStatus() != OrderStatus.PAID) {
			throw new InvalidOrderStatusException();
		}

		// 4. 주문 수락 처리
		order.accept();

		// 5. 최신 결제 정보 조회 후 응답 반환
		Payment payment = orderPaymentProcessor.getLatestPaymentOrNull(order.getId());
		return UpdateOrderStatusResponse.from(order, payment);
	}

	@Override
	@Transactional
	public UpdateOrderStatusResponse rejectOrder(UUID orderId, UUID storeId, String reason) {

		// 1. 주문 조회
		Order order = orderRepository.findById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		// 2. 해당 가게 주문인지 확인
		if (!order.getStore().getId().equals(storeId)) {
			throw new OrderForbiddenException();
		}

		// 3. 결제 완료 상태의 주문만 거절 가능
		if (order.getStatus() != OrderStatus.PAID) {
			throw new InvalidOrderStatusException();
		}

		// 4. 거절 사유 필수 검증
		if (!StringUtils.hasText(reason)) {
			throw new IllegalArgumentException("주문 거절 사유는 필수입니다.");
		}

		// 5. 환불 처리
		CancelPaymentQuery cancelPaymentQuery =
			orderPaymentProcessor.refundForRejectedOrder(order.getId(), reason);

		// 6. 주문 거절 처리
		order.reject(reason);

		// 7. 응답 반환
		return UpdateOrderStatusResponse.from(order, cancelPaymentQuery.getId(), cancelPaymentQuery.getStatus());
	}

	@Override
	@Transactional
	public UpdateOrderStatusResponse updateOrderStatusByStore(UUID orderId, UUID storeId,
		UpdateOrderStatusRequest request) {

		// 1. 주문 조회
		Order order = orderRepository.findById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		// 2. 해당 가게 주문인지 확인
		if (!order.getStore().getId().equals(storeId)) {
			throw new OrderForbiddenException();
		}

		// 3. 요청된 목표 상태 확인
		OrderStatus target = request.getStatus();

		// 4. 상태별 주문 처리
		if (target == OrderStatus.ACCEPTED) {
			order.accept();

		} else if (target == OrderStatus.COOKING) {
			order.startCooking();

		} else if (target == OrderStatus.DELIVERING) {
			order.startDelivering();

		} else if (target == OrderStatus.COMPLETED) {
			order.complete();

		} else if (target == OrderStatus.REJECTED) {
			// 상태 변경 API에서 거절 처리 시 기본 사유값 사용
			String reason = "STORE_STATUS_UPDATE";

			// 결제 환불 처리
			CancelPaymentQuery cancelPaymentQuery =
				orderPaymentProcessor.refundForRejectedOrder(order.getId(), reason);

			// 주문 거절 처리
			order.reject(reason);

			// 거절은 여기서 바로 응답 반환
			return UpdateOrderStatusResponse.from(
				order,
				cancelPaymentQuery.getId(),
				cancelPaymentQuery.getStatus()
			);

		} else {
			throw new InvalidOrderStatusException();
		}

		// 5. 일반 상태 변경은 최신 결제 정보와 함께 응답 반환
		Payment updatedPayment = orderPaymentProcessor.getLatestPaymentOrNull(order.getId());
		return UpdateOrderStatusResponse.from(order, updatedPayment);
	}
}