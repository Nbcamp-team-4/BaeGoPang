package com.team.project.domain.order.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.team.project.domain.order.api.request.CancelOrderRequest;
import com.team.project.domain.order.api.request.UpdateOrderStatusRequest;
import com.team.project.domain.order.api.response.CancelOrderResponse;
import com.team.project.domain.order.api.response.GetOrderDetailResponse;
import com.team.project.domain.order.api.response.GetOrderSummaryResponse;
import com.team.project.domain.order.api.response.UpdateOrderStatusResponse;
import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.exception.OrderNotFoundException;
import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.domain.order.repository.OrderRepository;
import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.repository.PaymentRepository;
import com.team.project.domain.payment.service.PaymentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderAdminServiceImpl implements OrderAdminService {

	private final OrderRepository orderRepository;
	private final PaymentRepository paymentRepository;
	private final PaymentService paymentService;

	@Override
	public List<GetOrderSummaryResponse> getAllOrders() {

		// 1) 전체 주문 조회
		List<Order> orders = orderRepository.findAll();

		// 2) 각 주문마다 최신 결제를 조회해서 응답 변환
		return orders.stream()
			.map(order -> {
				Payment payment = paymentRepository.getLatestPaymentByOrderId(order.getId())
					.orElse(null);
				return GetOrderSummaryResponse.from(order, payment);
			})
			.collect(Collectors.toList());
	}

	@Override
	public GetOrderDetailResponse getOrderDetail(UUID orderId) {

		// 1) 주문 상세 조회
		Order order = orderRepository.findDetailById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		// 2) 최신 결제 조회
		Payment payment = paymentRepository.getLatestPaymentByOrderId(order.getId())
			.orElse(null);

		// 3) 주문 + 결제 요약 응답 반환
		return GetOrderDetailResponse.from(order, payment);
	}

	@Override
	@Transactional
	public CancelOrderResponse cancelOrder(UUID orderId, CancelOrderRequest request) {

		// 1) 주문 조회
		Order order = orderRepository.findById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		// 2) 취소 사유 추출
		String reason = request == null ? null : request.getReason();

		// 5) 취소 후 최신 결제 재조회
		Payment updatedPayment = paymentRepository.getLatestPaymentByOrderId(order.getId())
			.orElse(null);

		return CancelOrderResponse.from(order, updatedPayment.getId(), updatedPayment.getStatus());
	}

	@Override
	@Transactional
	public UpdateOrderStatusResponse updateOrderStatus(UUID orderId, UpdateOrderStatusRequest request) {

		// 1) 주문 조회
		Order order = orderRepository.findById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		// 2) 최신 결제 조회
		Payment latestPayment = paymentRepository.getLatestPaymentByOrderId(order.getId())
			.orElse(null);

		// 3) 강제 상태 변경
		OrderStatus target = request.getStatus();

		// 4) 변경 후 최신 결제 재조회
		Payment updatedPayment = paymentRepository.getLatestPaymentByOrderId(order.getId())
			.orElse(null);

		return UpdateOrderStatusResponse.from(order, updatedPayment);
	}
}