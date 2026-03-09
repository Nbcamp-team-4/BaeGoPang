package com.team.project.domain.region.service;

import java.util.UUID;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.region.api.request.CreateRegionRequest;
import com.team.project.domain.region.api.request.RegionSearchRequest;
import com.team.project.domain.region.api.request.UpdateRegionRequest;
import com.team.project.domain.region.api.response.PagedRegionsResponse;
import com.team.project.domain.region.api.response.RegionResponse;

public interface RegionService {

    RegionResponse createRegion(UserDto userDto, CreateRegionRequest request);

    RegionResponse getRegion(UUID regionId);

    PagedRegionsResponse getRegionsForUser(RegionSearchRequest request);

    PagedRegionsResponse getRegionsForAdmin(RegionSearchRequest request);

    RegionResponse updateRegion(UserDto userDto, UUID regionId, UpdateRegionRequest request);

    void deactivateRegion(UserDto userDto, UUID regionId);

    void activateRegion(UserDto userDto, UUID regionId);
}