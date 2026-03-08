package com.team.project.domain.payment_log.model.dto;

import java.util.UUID;

import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePaymentLogCommand {

	private String paymentKey;
	private PaymentLogStatus status;
	private String reason;
	private UUID paymentId;

	public static CreatePaymentLogCommand of(String paymentKey, PaymentLogStatus status, String reason,
		UUID paymentId) {
		return new CreatePaymentLogCommand(paymentKey, status, reason, paymentId);
	}
}
