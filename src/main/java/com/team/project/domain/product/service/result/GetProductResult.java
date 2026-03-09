package com.team.project.domain.product.service.result;

import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetProductResult {

	private final UUID id;
	private final UUID storeId;
	private final String name;
	private final Integer price;
	private final String description;
	private final Boolean useAiDescription;
	private final String imageUrl;
	private final Boolean isSoldOut;
	private final Boolean isHidden;
	private final List<OptionGroup> options;

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class OptionGroup {
		private final UUID optionId;
		private final String name;
		private final Boolean isRequired;
		private final List<OptionItem> items;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class OptionItem {
		private final UUID itemId;
		private final String name;
		private final Integer additionalPrice;
	}
}