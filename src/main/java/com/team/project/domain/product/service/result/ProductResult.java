package com.team.project.domain.product.service.result;

import java.util.UUID;

import com.team.project.domain.product.entity.Product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductResult {

	private final UUID id;
	private final UUID storeId;
	private final String name;
	private final Integer price;
	private final String description;
	private final Boolean useAiDescription;
	private final String imageUrl;
	private final Boolean isSoldOut;
	private final Boolean isHidden;

	public static ProductResult from(Product product) {
		return new ProductResult(
			product.getId(),
			product.getStore().getId(),
			product.getName(),
			product.getPrice(),
			product.getDescription(),
			product.isUseAiDescription(),
			product.getImageUrl(),
			product.isSoldOut(),
			product.isHidden()
		);
	}
}