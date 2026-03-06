package com.team.project.domain.payment.api.request;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PayPaymentRequest {
	@NotNull
	private UUID orderId;
	@NotNull
	String paymentKey;
	@NotNull
	Integer amount;
}
