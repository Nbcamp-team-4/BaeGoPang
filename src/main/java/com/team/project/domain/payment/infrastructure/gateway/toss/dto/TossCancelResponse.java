package com.team.project.domain.payment.infrastructure.gateway.toss.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TossCancelResponse {

	private String mId;
	private String paymentKey;
	private String orderId;
	private String currency;
	private String method;
	private String status;
	private String type;
	private String country;

	private Integer totalAmount;
	private Integer balanceAmount;

	private List<Cancel> cancels;

	@Getter
	@Builder
	public static class Cancel {

		private String cancelReason;
		private String canceledAt;
		private Integer cancelAmount;

	}
}