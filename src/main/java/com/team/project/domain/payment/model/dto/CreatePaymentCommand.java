package com.team.project.domain.payment.model.dto;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePaymentCommand {

	@NotNull
	private UUID orderId;
	@NotNull
	private Integer amount;

}
