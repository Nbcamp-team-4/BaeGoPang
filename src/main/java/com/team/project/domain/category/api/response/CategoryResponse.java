package com.team.project.domain.category.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.category.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {

    private UUID id;
    private String name;
    private LocalDateTime createdAt;

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getCreatedAt()
        );
    }
}