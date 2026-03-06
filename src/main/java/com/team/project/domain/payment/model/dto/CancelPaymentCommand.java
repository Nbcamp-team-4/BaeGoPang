package com.team.project.domain.payment.model.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CancelPaymentCommand {
	UUID orderId;
	String reason;
}
