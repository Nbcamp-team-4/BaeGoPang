package com.team.project.domain.payment.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CancelPgProviderPaymentCommand {

	private String paymentKey;
	private String reason;

	public static CancelPgProviderPaymentCommand of(String paymentKey, String reason) {
		return new CancelPgProviderPaymentCommand(paymentKey, reason);
	}
}
