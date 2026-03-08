package com.team.project.domain.payment.model.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CancelPaymentCommand {
	UUID orderId;
	String reason;
	String type; // "REFUND" 혹은 "CANCEL"이여야 함

	public static CancelPaymentCommand of(UUID orderId, String reason, String type) {
		return new CancelPaymentCommand(orderId, reason, type);
	}

	public static CancelPaymentCommand ofRefund(UUID orderId, String reason) {
		return new CancelPaymentCommand(orderId, reason, "REFUND");
	}

	public static CancelPaymentCommand ofCancel(UUID orderId, String reason) {
		return new CancelPaymentCommand(orderId, reason, "CANCEL");
	}
}
