package com.team.project.domain.payment.model.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CancelPaymentCommand {
	UUID orderId;
	String reason;

	public static CancelPaymentCommand of(UUID orderId, String reason) {
		return new CancelPaymentCommand(orderId, reason);
	}
}
