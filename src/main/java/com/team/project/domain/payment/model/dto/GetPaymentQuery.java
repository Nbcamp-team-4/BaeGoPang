package com.team.project.domain.payment.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPaymentQuery {
	private UUID id;
	private PaymentStatus status;
	private Integer amount;
	private LocalDateTime createdAt;
	private UUID createdBy;
	private LocalDateTime updatedAt;
	private UUID updatedBy;

	public static GetPaymentQuery from(Payment payment) {
		return GetPaymentQuery.builder()
			.id(payment.getId())
			.status(payment.getStatus())
			.amount(payment.getAmount())
			.createdAt(payment.getCreatedAt())
			.createdBy(payment.getCreatedBy())
			.updatedAt(payment.getUpdatedAt())
			.updatedBy(payment.getUpdatedBy())
			.build();
	}
}
