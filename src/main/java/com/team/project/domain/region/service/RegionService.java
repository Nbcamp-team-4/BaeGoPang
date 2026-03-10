package com.team.project.domain.region.service;

import java.util.UUID;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.region.api.request.CreateRegionRequest;
import com.team.project.domain.region.api.request.RegionSearchRequest;
import com.team.project.domain.region.api.request.UpdateRegionRequest;
import com.team.project.domain.region.api.response.RegionResponse;
import com.team.project.global.common.dto.BasePageResponse;

public interface RegionService {

    RegionResponse createRegion(UserDto userDto, CreateRegionRequest request);

    RegionResponse getRegion(UUID regionId);

    BasePageResponse<RegionResponse> getRegionsForUser(RegionSearchRequest request);

    BasePageResponse<RegionResponse> getRegionsForAdmin(RegionSearchRequest request);

    RegionResponse updateRegion(UserDto userDto, UUID regionId, UpdateRegionRequest request);

    // 운영상 활성/비활성 전환
    void deactivateRegion(UserDto userDto, UUID regionId);

    void activateRegion(UserDto userDto, UUID regionId);
}