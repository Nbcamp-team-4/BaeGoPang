package com.team.project.domain.category.api.response;

import java.util.List;

import com.team.project.global.common.dto.BasePageResponse;

public class GetCategoriesResponse extends BasePageResponse<CategoryResponse> {

    public GetCategoriesResponse(
        List<CategoryResponse> content,
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages
    ) {
        super(content, page, size, totalElements, totalPages);
    }
}