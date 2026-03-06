package com.team.project.domain.payment.model.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PayPaymentCommand {
	UUID orderId;
	String paymentKey;
	Integer amount;
}
