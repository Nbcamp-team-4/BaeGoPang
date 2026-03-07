package com.team.project.domain.payment.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConfirmPgProviderPaymentCommand {
	private String paymentKey;
	private String orderId;
	private Integer amount;
}
