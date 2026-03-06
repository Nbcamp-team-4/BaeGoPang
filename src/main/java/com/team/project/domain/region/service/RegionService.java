package com.team.project.domain.region.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.team.project.domain.region.api.request.CreateRegionRequest;
import com.team.project.domain.region.api.request.UpdateRegionRequest;
import com.team.project.domain.region.api.response.PagedRegionsResponse;
import com.team.project.domain.region.api.response.RegionResponse;

public interface RegionService {

    RegionResponse createRegion(CreateRegionRequest request);

    RegionResponse getRegion(UUID regionId);

    // ✅ 사용자용(활성만)
    PagedRegionsResponse getRegionsForUser(Pageable pageable);

    // ✅ 관리자용(전체)
    PagedRegionsResponse getRegionsForAdmin(Pageable pageable);

    RegionResponse updateRegion(UUID regionId, UpdateRegionRequest request);

    void deactivateRegion(UUID regionId);
    void activateRegion(UUID regionId);
}