package com.team.project.domain.payment.model.dto;

import javax.validation.constraints.NotNull;

import com.team.project.domain.order.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePaymentCommand {

	@NotNull
	private Order order;
	@NotNull
	private Integer amount;

	public static CreatePaymentCommand of(Order order, Integer amount) {
		return new CreatePaymentCommand(order, amount);
	}
}
