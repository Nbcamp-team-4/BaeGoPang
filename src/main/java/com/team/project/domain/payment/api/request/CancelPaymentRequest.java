package com.team.project.domain.payment.api.request;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CancelPaymentRequest {

	private UUID orderId;
	private String reason;
}
