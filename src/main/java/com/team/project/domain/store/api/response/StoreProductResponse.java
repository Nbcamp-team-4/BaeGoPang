package com.team.project.domain.store.api.response;

import java.util.UUID;

import com.team.project.domain.product.entity.Product;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreProductResponse {

	private UUID productId;
	private String name;
	private Integer price;
	private String imageUrl;

	public static StoreProductResponse from(Product product) {
		return StoreProductResponse.builder()
			.productId(product.getId())
			.name(product.getName())
			.price(product.getPrice())
			.imageUrl(product.getImageUrl())
			.build();
	}
}