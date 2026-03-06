package com.team.project.domain.region.api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PagedRegionsResponse {

    private List<RegionResponse> items;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}