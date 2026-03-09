package com.team.project.domain.product.api.response;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.product.service.result.GetProductResult;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetProductResponse {

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

	public static GetProductResponse from(GetProductResult result) {
		return GetProductResponse.builder()
			.id(result.getId())
			.storeId(result.getStoreId())
			.name(result.getName())
			.price(result.getPrice())
			.description(result.getDescription())
			.useAiDescription(result.getUseAiDescription())
			.imageUrl(result.getImageUrl())
			.isSoldOut(result.getIsSoldOut())
			.isHidden(result.getIsHidden())
			.options(
				result.getOptions().stream()
					.map(option -> OptionGroup.builder()
						.optionId(option.getOptionId())
						.name(option.getName())
						.isRequired(option.getIsRequired())
						.items(
							option.getItems().stream()
								.map(item -> OptionItem.builder()
									.itemId(item.getItemId())
									.name(item.getName())
									.additionalPrice(item.getAdditionalPrice())
									.build())
								.toList()
						)
						.build())
					.toList()
			)
			.build();
	}

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