package com._team._project.domain.region.service;

import com._team._project.domain.region.api.request.CreateRegionRequest;
import com._team._project.domain.region.api.request.UpdateRegionRequest;
import com._team._project.domain.region.api.response.GetRegionsResponse;
import com._team._project.domain.region.api.response.RegionResponse;
import com._team._project.domain.region.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public RegionResponse createRegion(CreateRegionRequest request) {
        return null;
    }

    @Override
    public RegionResponse getRegion(UUID regionId) {
        return null;
    }

    @Override
    public GetRegionsResponse getRegions() {
        return null;
    }

    @Override
    public RegionResponse updateRegion(UUID regionId, UpdateRegionRequest request) {
        return null;
    }

    @Override
    public void deactivateRegion(UUID regionId) {

    }
}