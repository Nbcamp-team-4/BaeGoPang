package com.team.project.domain.payment.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConfirmPgProviderPaymentCommand {
	private String paymentKey;
	private String orderId;
	private Integer amount;

	public static ConfirmPgProviderPaymentCommand of(String paymentKey, String orderId, Integer amount) {
		return new ConfirmPgProviderPaymentCommand(paymentKey, orderId, amount);
	}
}
