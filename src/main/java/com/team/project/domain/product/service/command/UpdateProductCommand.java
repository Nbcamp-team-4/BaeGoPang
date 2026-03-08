package com.team.project.domain.product.service.command;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProductCommand {

	private UUID productId;
	private String name;
	private Integer price;
	private String description;
	private Boolean useAiDescription;
	private String imageUrl;
	private List<UpdateOptionGroupCommand> options;

	@Getter
	@Builder
	public static class UpdateOptionGroupCommand {
		private UUID optionId;
		private String name;
		private Boolean isRequired;
		private List<UpdateOptionItemCommand> items;
	}

	@Getter
	@Builder
	public static class UpdateOptionItemCommand {
		private UUID itemId;
		private String name;
		private Integer additionalPrice;
	}
}