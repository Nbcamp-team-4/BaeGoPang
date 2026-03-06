package com.team.project.domain.payment.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PayPaymentQuery {

	private UUID id;
	private PaymentStatus status;
	private Integer amount;
	private LocalDateTime paidAt;
	private LocalDateTime updatedAt;
	private UUID updatedBy;

	public static PayPaymentQuery from(Payment payment) {
		return PayPaymentQuery.builder()
			.id(payment.getId())
			.status(payment.getStatus())
			.amount(payment.getAmount())
			.paidAt(payment.getPaidAt())
			.updatedAt(payment.getUpdatedAt())
			.updatedBy(payment.getUpdatedBy())
			.build();
	}
}
