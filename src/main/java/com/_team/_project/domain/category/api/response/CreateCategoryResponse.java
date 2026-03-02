package com._team._project.domain.category.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCategoryResponse {
    private CategoryResponse category;
}
