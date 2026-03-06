package com.team.project.domain.payment.model.dto;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePaymentCommand {

	@NotNull
	private UUID orderId;
	@NotNull
	private Integer amount;

	public static CreatePaymentCommand of(UUID orderId, Integer amount) {
		return new CreatePaymentCommand(orderId, amount);
	}
}
