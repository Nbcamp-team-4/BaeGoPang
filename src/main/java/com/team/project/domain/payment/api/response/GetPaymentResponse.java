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
	private UUID id;
	private PaymentStatus status;
	private Integer amount;
	private String orderNo;
	private String orderStatus;
	private String pgCode;
	private String pgName;
	private LocalDateTime createdAt;
	private UUID createdBy;
	private LocalDateTime updatedAt;
	private UUID updatedBy;

	public static GetPaymentResponse from(@MonotonicNonNull GetPaymentQuery payment) {
		return GetPaymentResponse.builder()
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
