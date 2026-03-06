package com.team.project.domain.payment.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import com.team.project.domain.payment.model.dto.GetPaymentQuery;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPaymentResponse {

	private UUID paymentId;
	private PaymentStatus paymentStatus;
	private Integer amount;
	private LocalDateTime paidAt;
	private LocalDateTime createdAt;
	private UUID createdBy;
	private LocalDateTime updatedAt;
	private UUID updatedBy;

	public static GetPaymentResponse from(@MonotonicNonNull GetPaymentQuery payment) {
		return GetPaymentResponse.builder()
				.paymentId(payment.getId())
				.paymentStatus(payment.getStatus())
				.amount(payment.getAmount())
				.paidAt(payment.getPaidAt())
				.createdAt(payment.getCreatedAt())
				.createdBy(payment.getCreatedBy())
				.updatedAt(payment.getUpdatedAt())
				.updatedBy(payment.getUpdatedBy())
				.build();
	}
}