package com.team.project.domain.product.api.request;

import java.util.UUID;

import com.team.project.domain.product.service.command.UpdateProductCommand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UpdateOptionItemRequest {

	private UUID itemId; // 있으면 수정, 없으면 생성

	@NotBlank
	private String name;

	@PositiveOrZero
	private Integer additionalPrice;

	public UpdateProductCommand.UpdateOptionItemCommand toCommand() {
		return UpdateProductCommand.UpdateOptionItemCommand.builder()
			.itemId(itemId)
			.name(name)
			.additionalPrice(additionalPrice == null ? 0 : additionalPrice)
			.build();
	}
}