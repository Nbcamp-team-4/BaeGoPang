package com.team.project.domain.payment.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CancelPaymentQuery {
	private UUID id;
	private PaymentStatus status;
	private Integer amount;
	private String paymentKey;
	private LocalDateTime paidAt;

	public static CancelPaymentQuery from(Payment payment) {
		return CancelPaymentQuery.builder()
			.id(payment.getId())
			.status(payment.getStatus())
			.amount(payment.getAmount())
			.paymentKey(payment.getPaymentKey())
			.paidAt(payment.getPaidAt())
			.build();
	}
}
