package com._team._project.domain.region.service;

import java.util.UUID;

import com._team._project.domain.region.api.request.CreateRegionRequest;
import com._team._project.domain.region.api.request.UpdateRegionRequest;
import com._team._project.domain.region.api.response.CreateRegionResponse;
import com._team._project.domain.region.api.response.GetRegionResponse;
import com._team._project.domain.region.api.response.GetRegionsResponse;
import com._team._project.domain.region.api.response.UpdateRegionResponse;

public interface RegionService {

    CreateRegionResponse createRegion(CreateRegionRequest request);

    GetRegionResponse getRegion(UUID regionId);

    GetRegionsResponse getRegions();

    UpdateRegionResponse updateRegion(UUID regionId, UpdateRegionRequest request);

    void deleteRegion(UUID regionId);
}