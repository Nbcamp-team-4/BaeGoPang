package com.team.project.domain.payment.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment.model.dto.CreatePaymentQuery;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePaymentResponse {

	private UUID orderId;
	private UUID paymentId;
	private PaymentStatus paymentStatus;
	private Integer amount;
	private LocalDateTime createdAt;
	private UUID createdBy;

	public static CreatePaymentResponse from(CreatePaymentQuery query) {
		return CreatePaymentResponse.builder()
				.orderId(query.getOrderId())
				.paymentId(query.getPaymentId())
				.paymentStatus(query.getStatus())
				.amount(query.getAmount())
				.createdAt(query.getCreatedAt())
				.createdBy(query.getCreatedBy())
				.build();
	}
}