package com.team.project.domain.payment.infrastructure.toss.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TossConfirmResponse {
	private String paymentKey;
	private String orderId;
	private String status;
	private Integer totalAmount;

}
