package com.team.project.domain.region.service;

import com.team.project.domain.region.api.request.CreateRegionRequest;
import com.team.project.domain.region.api.request.UpdateRegionRequest;
import com.team.project.domain.region.api.response.PagedRegionsResponse;
import com.team.project.domain.region.api.response.RegionResponse;
import com.team.project.domain.region.entity.Region;
import com.team.project.domain.region.exception.RegionNotFoundException;
import com.team.project.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.WKTReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    public RegionResponse createRegion(CreateRegionRequest request) {
        MultiPolygon geom = toMultiPolygon(request.getGeomWkt());

        Region region = new Region(request.getName(), geom);

        if (request.getIsActive() != null && !request.getIsActive()) {
            region.deactivate();
        }

        return RegionResponse.from(regionRepository.save(region));
    }

    @Override
    @Transactional(readOnly = true)
    public RegionResponse getRegion(UUID regionId) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(RegionNotFoundException::new);
        return RegionResponse.from(region);
    }

    // 사용자: 활성만
    @Override
    @Transactional(readOnly = true)
    public PagedRegionsResponse getRegionsForUser(Pageable pageable) {
        Page<Region> page = regionRepository.findAllByActiveTrueOrderByCreatedAtDesc(pageable);

        return new PagedRegionsResponse(
                page.getContent().stream().map(RegionResponse::from).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    // 관리자: 전체
    @Override
    @Transactional(readOnly = true)
    public PagedRegionsResponse getRegionsForAdmin(Pageable pageable) {
        Page<Region> page = regionRepository.findAllByOrderByCreatedAtDesc(pageable);

        return new PagedRegionsResponse(
                page.getContent().stream().map(RegionResponse::from).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public RegionResponse updateRegion(UUID regionId, UpdateRegionRequest request) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(RegionNotFoundException::new);

        MultiPolygon geom = toMultiPolygon(request.getGeomWkt());
        region.updateInfo(request.getName(), geom);

        if (request.getIsActive() != null) {
            if (request.getIsActive()) region.activate();
            else region.deactivate();
        }

        return RegionResponse.from(region);
    }

    @Override
    public void deactivateRegion(UUID regionId) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(RegionNotFoundException::new);
        region.deactivate();
    }

    @Override
    public void activateRegion(UUID regionId) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(RegionNotFoundException::new);
        region.activate();
    }

    private MultiPolygon toMultiPolygon(String wkt) {
        try {
            Geometry g = new WKTReader().read(wkt);
            g.setSRID(4326);
            if (!(g instanceof MultiPolygon)) {
                throw new IllegalArgumentException("geomWkt는 MULTIPOLYGON 형식이어야 합니다.");
            }
            return (MultiPolygon) g;
        } catch (Exception e) {
            throw new IllegalArgumentException("geomWkt 형식 오류: MULTIPOLYGON(((...)))");
        }
    }
}