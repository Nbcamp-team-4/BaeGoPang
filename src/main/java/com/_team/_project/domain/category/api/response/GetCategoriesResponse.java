package com._team._project.domain.category.api.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetCategoriesResponse {
    private List<CategoryResponse> categories;
}
