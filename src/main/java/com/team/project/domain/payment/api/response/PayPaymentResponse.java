package com.team.project.domain.payment.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import com.team.project.domain.payment.model.dto.PayPaymentQuery;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PayPaymentResponse {
	private UUID id;
	private PaymentStatus status;
	private Integer amount;
	private LocalDateTime paidAt;
	private LocalDateTime updatedAt;
	private UUID updatedBy;

	public static PayPaymentResponse from(@MonotonicNonNull PayPaymentQuery payment) {
		return PayPaymentResponse.builder()
			.id(payment.getId())
			.status(payment.getStatus())
			.amount(payment.getAmount())
			.paidAt(payment.getPaidAt())
			.updatedAt(payment.getUpdatedAt())
			.updatedBy(payment.getUpdatedBy())
			.build();
	}
}
