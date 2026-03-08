package com.team.project.domain.payment.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CancelRequestPaymentResponse {
	private UUID id;
	private PaymentStatus status;
	private Integer amount;
	private String paymentKey;
	private LocalDateTime paidAt;

	public static CancelRequestPaymentResponse from(Payment payment) {
		return CancelRequestPaymentResponse.builder()
			.id(payment.getId())
			.status(payment.getStatus())
			.amount(payment.getAmount())
			.paymentKey(payment.getPaymentKey())
			.paidAt(payment.getPaidAt())
			.build();
	}
}
