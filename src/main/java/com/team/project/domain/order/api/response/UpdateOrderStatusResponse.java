package com.team.project.domain.order.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateOrderStatusResponse {

	// 주문 ID
	private UUID id;

	// 주문 번호
	private String orderNo;

	// 주문 상태
	private OrderStatus status;

	// 결제 ID
	private UUID paymentId;

	// 결제 상태
	private PaymentStatus paymentStatus;

	// 수정일시
	private LocalDateTime updatedAt;

	// 수정자
	private UUID updatedBy;

	public static UpdateOrderStatusResponse from(Order order, UUID paymentId, PaymentStatus paymentStatus) {
		return UpdateOrderStatusResponse.builder()
			.id(order.getId())
			.orderNo(order.getOrderNo())
			.status(order.getStatus())
			.paymentId(paymentId)
			.paymentStatus(paymentStatus)
			.updatedAt(order.getUpdatedAt())
			.updatedBy(order.getUpdatedBy())
			.build();
	}
}