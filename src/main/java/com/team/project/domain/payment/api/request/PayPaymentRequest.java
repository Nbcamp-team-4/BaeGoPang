package com.team.project.domain.payment.api.request;

import java.util.UUID;

import lombok.Data;

@Data
public class PayPaymentRequest {
	UUID orderId;
	String paymentKey;
	Integer amount;
}
