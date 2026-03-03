package com._team._project.domain.category.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetCategoryResponse {

    private CategoryResponse category;

    public static GetCategoryResponse of(CategoryResponse category) {
        return new GetCategoryResponse(category);
    }
}