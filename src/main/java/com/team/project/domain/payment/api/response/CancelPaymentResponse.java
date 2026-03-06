package com.team.project.domain.payment.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment.model.dto.CancelPaymentQuery;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CancelPaymentResponse {

	private UUID paymentId;
	private PaymentStatus paymentStatus;
	private Integer amount;
	private String paymentKey;
	private LocalDateTime paidAt;

	public static CancelPaymentResponse from(CancelPaymentQuery query) {
		return CancelPaymentResponse.builder()
				.paymentId(query.getId())
				.paymentStatus(query.getStatus())
				.amount(query.getAmount())
				.paymentKey(query.getPaymentKey())
				.paidAt(query.getPaidAt())
				.build();
	}
}