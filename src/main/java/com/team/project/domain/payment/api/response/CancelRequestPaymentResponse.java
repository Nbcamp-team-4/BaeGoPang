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

	private UUID paymentId;
	private PaymentStatus paymentStatus;
	private Integer amount;
	private String paymentKey;
	private LocalDateTime paidAt;

	public static CancelRequestPaymentResponse from(Payment payment) {
		return CancelRequestPaymentResponse.builder()
				.paymentId(payment.getId())
				.paymentStatus(payment.getStatus())
				.amount(payment.getAmount())
				.paymentKey(payment.getPaymentKey())
				.paidAt(payment.getPaidAt())
				.build();
	}
}