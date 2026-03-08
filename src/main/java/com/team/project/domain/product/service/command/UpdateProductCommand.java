package com.team.project.domain.product.service.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductCommand {

	private UUID productId;
	private String name;
	private Integer price;
	private String description;
	private Boolean useAiDescription;
	private String imageUrl;
}