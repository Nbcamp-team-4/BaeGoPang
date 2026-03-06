package com.team.project.domain.payment.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePaymentQuery {
	private UUID orderId;
	private UUID paymentId;
	private PaymentStatus status;
	private Integer amount;
	private LocalDateTime createdAt;
	private UUID createdBy;

	public static CreatePaymentQuery from(UUID orderId, Payment payment) {
		return CreatePaymentQuery.builder()
			.orderId(orderId)
			.paymentId(payment.getId())
			.status(payment.getStatus())
			.amount(payment.getAmount())
			.createdAt(payment.getCreatedAt())
			.createdBy(payment.getCreatedBy())
			.build();
	}
}
