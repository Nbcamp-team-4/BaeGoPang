package com.team.project.domain.payment_log.model.dto;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePaymentLogCommand {

	private String paymentKey;
	private PaymentLogStatus status;
	private String reason;
	private Payment payment;
}
