package com.team.project.domain.product.api.request;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.product.service.command.UpdateProductCommand;

import lombok.Data;

@Data
public class UpdateOptionGroupRequest {

	private UUID optionId; // 있으면 수정, 없으면 생성
	private String name;
	private Boolean isRequired;

	private List<UpdateOptionItemRequest> items;

	public UpdateProductCommand.UpdateOptionGroupCommand toCommand() {
		return UpdateProductCommand.UpdateOptionGroupCommand.builder()
			.optionId(optionId)
			.name(name)
			.isRequired(isRequired)
			.items(
				items == null ? List.of() : items.stream()
					.map(UpdateOptionItemRequest::toCommand)
					.toList()
			)
			.build();
	}
}