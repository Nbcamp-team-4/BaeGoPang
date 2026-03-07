package com.team.project.domain.payment.model.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PayPaymentCommand {
	UUID orderId;
	String paymentKey;
	Integer amount;

	public static PayPaymentCommand of(UUID orderId, String paymentKey, Integer amount) {
		return new PayPaymentCommand(orderId, paymentKey, amount);
	}
}
