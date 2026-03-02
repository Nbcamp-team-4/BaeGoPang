package com._team._project.domain.region.service;

import com._team._project.domain.region.api.request.CreateRegionRequest;
import com._team._project.domain.region.api.request.UpdateRegionRequest;
import com._team._project.domain.region.api.response.GetRegionsResponse;
import com._team._project.domain.region.api.response.RegionResponse;

import java.util.UUID;

public interface RegionService {

    RegionResponse createRegion(CreateRegionRequest request);

    RegionResponse getRegion(UUID regionId);

    GetRegionsResponse getRegions(); // 래핑 유지

    RegionResponse updateRegion(UUID regionId, UpdateRegionRequest request);

    void deactivateRegion(UUID regionId);
}