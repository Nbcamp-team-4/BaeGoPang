package com._team._project.domain.region.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PagedRegionsResponse {

    private List<RegionResponse> items;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}