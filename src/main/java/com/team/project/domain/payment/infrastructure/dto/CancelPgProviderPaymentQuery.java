package com.team.project.domain.payment.infrastructure.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CancelPgProviderPaymentQuery {

	private String paymentKey;
	private UUID orderId;
	private String status;
	private String cancelReason;
	private String canceledAt;
	private Integer cancelAmount;
}
