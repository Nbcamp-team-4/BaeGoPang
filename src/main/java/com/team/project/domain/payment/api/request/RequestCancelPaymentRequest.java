package com.team.project.domain.payment.api.request;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCancelPaymentRequest {
	private UUID orderId;
	private String reason;

}
