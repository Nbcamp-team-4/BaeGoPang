package com.team.project.domain.payment.model.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestCancelPaymentCommand {

	UUID orderId;
	String reason;

	public static RequestCancelPaymentCommand of(UUID orderId, String reason) {
		return new RequestCancelPaymentCommand(orderId, reason);
	}
}
