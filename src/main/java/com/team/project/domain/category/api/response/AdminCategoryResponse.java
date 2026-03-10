package com.team.project.domain.category.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.category.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminCategoryResponse {

	private UUID id;
	private String name;
	private LocalDateTime createdAt;
	private boolean deleted;

	public static AdminCategoryResponse from(Category category) {
		return new AdminCategoryResponse(
			category.getId(),
			category.getName(),
			category.getCreatedAt(),
			category.getDeletedAt() != null
		);
	}
}