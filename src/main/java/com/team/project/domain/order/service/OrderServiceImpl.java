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
import com.team.project.domain.payment.model.dto.CancelPaymentCommand;
import com.team.project.domain.payment.model.dto.CancelPaymentQuery;
import com.team.project.domain.payment.model.dto.CreatePaymentCommand;
import com.team.project.domain.payment.model.dto.CreatePaymentQuery;
import com.team.project.domain.payment.model.dto.PayPaymentCommand;
import com.team.project.domain.payment.model.dto.PayPaymentQuery;
import com.team.project.domain.payment.repository.PaymentRepository;
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
	private final PaymentRepository paymentRepository;

	@Override
	@Transactional
	public CreateOrderResponse createOrder(CreateOrderRequest request) {

		// 1) 유저/가게 존재 확인
		User user = userRepository.findById(request.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

		Store store = storeRepository.findById(request.getStoreId())
			.orElseThrow(() -> new IllegalArgumentException("STORE_NOT_FOUND"));

		// 2) 주문번호 생성
		String orderNo = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

		// 3) 총액 계산
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

		// 4) 배송지 조회
		UserAddress deliveryAddress = null;
		if (request.getDeliveryAddressId() != null) {
			deliveryAddress = userAddressRepository
				.findByIdAndUserId(request.getDeliveryAddressId(), request.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("DELIVERY_ADDRESS_NOT_FOUND"));
		}

		// 5) 주문 생성
		Order order = new Order(
			user,
			store,
			deliveryAddress,
			orderNo,
			totalAmount,
			request.getRequestMemo()
		);

		// 6) 주문 아이템/옵션 생성
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

		// 7) 저장
		Order saved = orderRepository.save(order);

		// 8) 결제 생성
		CreatePaymentQuery paymentQuery = paymentService.createPayment(
			CreatePaymentCommand.of(saved, saved.getTotalAmount())); // 응답에 사용하시면 payment 상태까지 같이 보낼수 있습니다.

		return CreateOrderResponse.from(saved);
	}

	@Override
	@Transactional
	public ConfirmOrderPaymentResponse confirmOrderPayment(UUID orderId, ConfirmOrderPaymentRequest request) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
			throw new InvalidOrderStatusException();
		}

		PayPaymentQuery payPaymentQuery = paymentService.payPayment(
			PayPaymentCommand.of(orderId, request.getPaymentKey(), request.getAmount())
		);

		order.markPaid();

		return ConfirmOrderPaymentResponse.from(order, payPaymentQuery);
	}

	@Override
	public GetOrderDetailResponse getOrderDetail(UUID orderId, UUID userId) {
		Order order = orderRepository.findDetailByIdAndUserId(orderId, userId)
			.orElseThrow(OrderNotFoundException::new);

		Payment payment = paymentRepository.getLatestPaymentByOrderId(order.getId())
			.orElse(null);

		return GetOrderDetailResponse.from(order, payment);
	}

	@Override
	public List<GetOrderSummaryResponse> getMyOrders(UUID userId) {
		List<Order> orders = orderRepository.findAllByUserIdOrderByOrderDateDesc(userId);

		return orders.stream()
			.map(order -> {
				Payment payment = paymentRepository.getLatestPaymentByOrderId(order.getId())
					.orElse(null);
				return GetOrderSummaryResponse.from(order, payment);
			})
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public CancelOrderResponse cancelOrder(UUID orderId, UUID userId, CancelOrderRequest request) {

		Order order = orderRepository.findDetailById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		if (!order.getUser().getId().equals(userId)) {
			throw new OrderForbiddenException();
		}

		if (order.getStatus() == OrderStatus.CANCELED) {
			throw new OrderAlreadyCanceledException();
		}

		if (order.getStatus() == OrderStatus.COMPLETED) {
			throw new OrderCannotCancelException();
		}

		if (order.getStatus() != OrderStatus.PENDING_PAYMENT
			&& order.getStatus() != OrderStatus.PAID
			&& order.getStatus() != OrderStatus.ACCEPTED) {
			throw new InvalidOrderStatusException();
		}

		// 취소 사유는 항상 들어와야 합니다.
		if (request == null || !StringUtils.hasText(request.getReason())) {
			throw new InvalidOrderStatusException(); // 추후 더 적절한 예외로 변경 가능
		}

		// 결제를 취소합니다.
		// 결제를 취소할 수 없는 상황이라면 두 가지 선택지가 있습니다. (원하시는 상황을 선택하셔서 지우시면 됩니다.)
		// 1. 결제 취소할 수 없을 때 주문 취소도 못하게 하고 싶은 경우 -> paymentService에서 에러가 나면 에러를 잡지 않거나 잡아서 주문 관련 에러를 던져서 롤백
		try {
			CancelPaymentQuery cancelPaymentQuery = paymentService.cancelPayment(
				CancelPaymentCommand.ofCancel(order.getId(), request.getReason()));

			order.cancel(request.getReason());

			return CancelOrderResponse.from(order, cancelPaymentQuery.getId(), cancelPaymentQuery.getStatus());
		} catch (Exception e) {
			throw new InvalidOrderStatusException(); // 추후 결제 취소 실패 전용 예외로 변경 가능
		}

		// 2. 결제 취소할 수 없든 말든 주문 취소는 하고 싶은 경우 -> try, catch로 에러를 잡고 finally에서 cancel을 호출하면 됩니다. 이경우 결제 취소는 안되므로 toss에 결제 취소가 안들어갑니다.
		// CancelPaymentQuery cancelPaymentQuery = null;
		// try{
		// 	cancelPaymentQuery = paymentService.cancelPayment(
		// 		CancelPaymentCommand.of(order.getId(), request.getReason()));
		// }catch(Exception e){
		// 	throw new InvalidOrderStatusException(); // 알맞는 exception으로 변경해주세요.
		// }finally{
		// 	order.cancel(request.getReason());
		// 	return CancelOrderResponse.from(order, cancelPaymentQuery.getId(), cancelPaymentQuery.getStatus());
		// }
	}

	@Override
	@Transactional
	public void deleteOrder(UUID orderId, UUID userId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		if (!order.getUser().getId().equals(userId)) {
			throw new OrderForbiddenException();
		}

		order.markDeleted(userId);
	}

	@Override
	public List<GetOrderSummaryResponse> getStoreOrders(UUID storeId) {
		List<Order> orders = orderRepository.findAllByStoreIdOrderByOrderDateDesc(storeId);

		return orders.stream()
			.map(order -> {
				Payment payment = paymentRepository.getLatestPaymentByOrderId(order.getId())
					.orElse(null);
				return GetOrderSummaryResponse.from(order, payment);
			})
			.collect(Collectors.toList());
	}

	@Override
	public GetOrderDetailResponse getStoreOrderDetail(UUID orderId, UUID storeId) {
		Order order = orderRepository.findDetailByIdAndStoreId(orderId, storeId)
			.orElseThrow(OrderNotFoundException::new);

		Payment payment = paymentRepository.getLatestPaymentByOrderId(order.getId())
			.orElse(null);

		return GetOrderDetailResponse.from(order, payment);
	}

	@Override
	@Transactional
	public UpdateOrderStatusResponse acceptOrder(UUID orderId, UUID storeId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		if (!order.getStore().getId().equals(storeId)) {
			throw new OrderForbiddenException();
		}

		if (order.getStatus() != OrderStatus.PAID) {
			throw new InvalidOrderStatusException();
		}

		order.accept();

		Payment payment = paymentRepository.getLatestPaymentByOrderId(order.getId())
			.orElse(null);

		return UpdateOrderStatusResponse.from(order, payment);
	}

	@Override
	@Transactional
	public UpdateOrderStatusResponse rejectOrder(UUID orderId, UUID storeId, String reason) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		if (!order.getStore().getId().equals(storeId)) {
			throw new OrderForbiddenException();
		}

		if (order.getStatus() != OrderStatus.PAID) {
			throw new InvalidOrderStatusException();
		}

		// 결제 완료 상태면 환불 처리까지 연결
		// 결제를 취소합니다.
		// 결제를 취소할 수 없는 상황이라면 두 가지 선택지가 있습니다. (원하시는 상황을 선택하셔서 지우시면 됩니다.)
		// 1. 결제 취소할 수 없을 때 주문 거절도 못하게 하고 싶은 경우 -> paymentService에서 에러가 나면 에러를 잡지 않거나 잡아서 주문 관련 에러를 던져서 롤백
		try {
			CancelPaymentQuery cancelPaymentQuery = paymentService.cancelPayment(
				CancelPaymentCommand.ofRefund(order.getId(), reason));

			order.reject(reason);

			return UpdateOrderStatusResponse.from(order, cancelPaymentQuery.getId(), cancelPaymentQuery.getStatus());
		} catch (Exception e) {
			throw new InvalidOrderStatusException(); // 추후 결제 환불 실패 전용 예외로 변경 가능
		}

		// 2. 결제 취소할 수 없든 말든 주문 거절을 하고 싶은 경우 -> try, catch로 에러를 잡고 finally에서 reject를 호출하면 됩니다. 이경우 결제 취소는 안되므로 toss에 결제 취소가 안들어갑니다.
		// CancelPaymentQuery cancelPaymentQuery = null;
		// try{
		// 	cancelPaymentQuery = paymentService.cancelPayment(
		// 		CancelPaymentCommand.of(order.getId(), reason));
		// }catch(Exception e){
		// 	throw new InvalidOrderStatusException(); // 알맞는 exception으로 변경해주세요.
		// }finally{
		// 	order.reject(reason);
		// 	return UpdateOrderStatusResponse.from(order, cancelPaymentQuery.getId(), cancelPaymentQuery.getStatus());
		// }

	}

	@Override
	@Transactional
	public UpdateOrderStatusResponse updateOrderStatusByStore(UUID orderId, UUID storeId,
		UpdateOrderStatusRequest request) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(OrderNotFoundException::new);

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
			// 결제 완료 상태면 환불 처리까지 연결
			// 결제를 취소합니다.
			// 결제를 취소할 수 없는 상황이라면 두 가지 선택지가 있습니다. (원하시는 상황을 선택하셔서 지우시면 됩니다.)
			String reason = "STORE_STATUS_UPDATE";
			// 1. 결제 취소할 수 없을 때 주문 거절도 못하게 하고 싶은 경우 -> paymentService에서 에러가 나면 에러를 잡지 않거나 잡아서 주문 관련 에러를 던져서 롤백
			try {
				CancelPaymentQuery cancelPaymentQuery = paymentService.cancelPayment(
					CancelPaymentCommand.ofRefund(order.getId(), reason));

				order.reject(reason);

				return UpdateOrderStatusResponse.from(order, cancelPaymentQuery.getId(),
					cancelPaymentQuery.getStatus());
			} catch (Exception e) {
				throw new InvalidOrderStatusException(); // 추후 결제 환불 실패 전용 예외로 변경 가능
			}

			// 2. 결제 취소할 수 없든 말든 주문 거절을 하고 싶은 경우 -> try, catch로 에러를 잡고 finally에서 reject를 호출하면 됩니다. 이경우 결제 취소는 안되므로 toss에 결제 취소가 안들어갑니다.
			// CancelPaymentQuery cancelPaymentQuery = null;
			// try{
			// 	cancelPaymentQuery = paymentService.cancelPayment(
			// 		CancelPaymentCommand.of(order.getId(), reason));
			// }catch(Exception e){
			// 	throw new InvalidOrderStatusException(); // 알맞는 exception으로 변경해주세요.
			// }finally{
			// 	order.reject(reason);
			// 	return UpdateOrderStatusResponse.from(order, cancelPaymentQuery.getId(), cancelPaymentQuery.getStatus());
			// }
		} else {
			throw new InvalidOrderStatusException();
		}

		Payment updatedPayment = paymentRepository.getLatestPaymentByOrderId(order.getId())
			.orElse(null);

		return UpdateOrderStatusResponse.from(order, updatedPayment);
	}
}