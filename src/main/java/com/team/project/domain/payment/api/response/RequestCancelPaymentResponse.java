package com.team.project.domain.payment.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import com.team.project.domain.payment.model.dto.RequestCancelPaymentQuery;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestCancelPaymentResponse {
	private UUID id;
	private PaymentStatus status;
	private Integer amount;
	private String paymentKey;
	private LocalDateTime paidAt;

	public static RequestCancelPaymentResponse from(@MonotonicNonNull RequestCancelPaymentQuery payment) {
		return RequestCancelPaymentResponse.builder()
			.id(payment.getId())
			.status(payment.getStatus())
			.amount(payment.getAmount())
			.paymentKey(payment.getPaymentKey())
			.paidAt(payment.getPaidAt())
			.build();
	}
}
