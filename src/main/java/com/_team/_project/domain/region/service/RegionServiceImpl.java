package com._team._project.domain.region.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.MultiPolygon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._team._project.domain.region.api.request.CreateRegionRequest;
import com._team._project.domain.region.api.request.UpdateRegionRequest;
import com._team._project.domain.region.api.response.CreateRegionResponse;
import com._team._project.domain.region.api.response.GetRegionResponse;
import com._team._project.domain.region.api.response.GetRegionsResponse;
import com._team._project.domain.region.api.response.UpdateRegionResponse;
import com._team._project.domain.region.entity.Region;
import com._team._project.domain.region.repository.RegionRepository;
import com._team._project.domain.region.service.RegionService;
import com._team._project.domain.region.util.RegionGeometryUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    public CreateRegionResponse createRegion(CreateRegionRequest request) {

        if (regionRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Region name already exists: " + request.getName());
        }

        MultiPolygon geom = RegionGeometryUtil.toMultiPolygon(request.getGeomWkt());
        boolean active = request.getIsActive() == null || request.getIsActive();

        Region region = new Region(request.getName(), geom, active);

        Region saved = regionRepository.saveAndFlush(region);

        // createdAt(DB default) 값을 응답에 확실히 담기 위해 재조회
        Region found = regionRepository.findById(saved.getId())
                .orElseThrow(() -> new IllegalArgumentException("Region not found after save: " + saved.getId()));

        return CreateRegionResponse.builder()
                .id(found.getId())
                .name(found.getName())
                .isActive(found.isActive())
                .createdAt(found.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public GetRegionResponse getRegion(UUID regionId) {

        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("Region not found: " + regionId));

        return GetRegionResponse.builder()
                .id(region.getId())
                .name(region.getName())
                .isActive(region.isActive())
                .createdAt(region.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public GetRegionsResponse getRegions() {

        List<GetRegionResponse> regions = regionRepository.findAll().stream()
                .map(region -> GetRegionResponse.builder()
                        .id(region.getId())
                        .name(region.getName())
                        .isActive(region.isActive())
                        .createdAt(region.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return GetRegionsResponse.builder()
                .regions(regions)
                .build();
    }

    @Override
    public UpdateRegionResponse updateRegion(UUID regionId, UpdateRegionRequest request) {

        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("Region not found: " + regionId));

        // name 변경 시 중복 체크 (본인 제외)
        regionRepository.getByName(request.getName()).ifPresent(found -> {
            if (!found.getId().equals(regionId)) {
                throw new IllegalArgumentException("Region name already exists: " + request.getName());
            }
        });

        MultiPolygon geom = RegionGeometryUtil.toMultiPolygon(request.getGeomWkt());
        boolean active = request.getIsActive() == null || request.getIsActive();

        region.update(request.getName(), geom, active);

        // JPA체크로 update 반영
        return UpdateRegionResponse.builder()
                .id(region.getId())
                .name(region.getName())
                .isActive(region.isActive())
                .createdAt(region.getCreatedAt())
                .build();
    }

    @Override
    public void deleteRegion(UUID regionId) {

        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("Region not found: " + regionId));

        regionRepository.delete(region);
    }
}