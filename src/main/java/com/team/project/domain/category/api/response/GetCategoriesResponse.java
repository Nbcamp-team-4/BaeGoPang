package com.team.project.domain.category.api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetCategoriesResponse {

    private List<CategoryResponse> items;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}