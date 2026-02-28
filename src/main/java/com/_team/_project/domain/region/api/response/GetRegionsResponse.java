package com._team._project.domain.region.api.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetRegionsResponse {

    private List<GetRegionResponse> regions;
}