package com.team.project.domain.payment.infrastructure.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConfirmPgProviderPaymentQuery {
	private String paymentKey;
	private UUID orderId;
	private Integer amount;
}
