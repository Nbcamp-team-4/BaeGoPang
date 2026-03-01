package com._team._project.domain.payment.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com._team._project.domain.payment.entity.Payment;
import com._team._project.domain.payment.model.vo.PaymentMethod;
import com._team._project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PayPaymentResponse {
	private UUID id;
	private PaymentStatus status;
	private PaymentMethod method;
	private Integer amount;
	private LocalDateTime updatedAt;
	private UUID updatedBy;

	public static PayPaymentResponse from(Payment payment) {
		return PayPaymentResponse.builder()
			.id(payment.getId())
			.status(payment.getStatus())
			.method(payment.getMethod())
			.amount(payment.getAmount())
			.updatedAt(payment.getUpdatedAt())
			.updatedBy(payment.getUpdatedBy())
			.build();
	}
}
