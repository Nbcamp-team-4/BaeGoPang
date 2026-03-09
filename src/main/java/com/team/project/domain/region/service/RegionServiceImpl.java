package com.team.project.domain.region.service;

import java.util.UUID;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.WKTReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.region.api.request.CreateRegionRequest;
import com.team.project.domain.region.api.request.RegionSearchRequest;
import com.team.project.domain.region.api.request.UpdateRegionRequest;
import com.team.project.domain.region.api.response.PagedRegionsResponse;
import com.team.project.domain.region.api.response.RegionResponse;
import com.team.project.domain.region.entity.Region;
import com.team.project.domain.region.exception.InvalidRegionGeomException;
import com.team.project.domain.region.exception.RegionNotFoundException;
import com.team.project.domain.region.repository.RegionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    public RegionResponse createRegion(UserDto userDto, CreateRegionRequest request) {
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

    @Override
    @Transactional(readOnly = true)
    public PagedRegionsResponse getRegionsForUser(RegionSearchRequest request) {
        Pageable pageable = PageRequest.of(
            request.getPage(),
            request.getSize(),
            Sort.by(Sort.Direction.DESC, "createdAt")
        );

        String keyword = normalizeKeyword(request.getKeyword());
        Page<Region> page;

        if (keyword == null) {
            page = regionRepository.findAllByActiveTrueOrderByCreatedAtDesc(pageable);
        } else {
            page = regionRepository.findAllByIsActiveTrueAndNameContainingIgnoreCaseOrderByCreatedAtDesc(
                keyword,
                pageable
            );
        }

        return new PagedRegionsResponse(
            page.getContent().stream().map(RegionResponse::from).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PagedRegionsResponse getRegionsForAdmin(RegionSearchRequest request) {
        Pageable pageable = PageRequest.of(
            request.getPage(),
            request.getSize(),
            Sort.by(Sort.Direction.DESC, "createdAt")
        );

        String keyword = normalizeKeyword(request.getKeyword());
        Page<Region> page;

        if (keyword == null) {
            page = regionRepository.findAllByOrderByCreatedAtDesc(pageable);
        } else {
            page = regionRepository.findAllByNameContainingIgnoreCaseOrderByCreatedAtDesc(
                keyword,
                pageable
            );
        }

        return new PagedRegionsResponse(
            page.getContent().stream().map(RegionResponse::from).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }

    @Override
    public RegionResponse updateRegion(UserDto userDto, UUID regionId, UpdateRegionRequest request) {
        Region region = regionRepository.findById(regionId)
            .orElseThrow(RegionNotFoundException::new);

        MultiPolygon geom = toMultiPolygon(request.getGeomWkt());
        region.updateInfo(request.getName(), geom);

        if (request.getIsActive() != null) {
            if (request.getIsActive()) {
                region.activate();
            } else {
                region.deactivate();
            }
        }

        return RegionResponse.from(region);
    }

    @Override
    public void deactivateRegion(UserDto userDto, UUID regionId) {
        Region region = regionRepository.findById(regionId)
            .orElseThrow(RegionNotFoundException::new);
        region.deactivate();
    }

    @Override
    public void activateRegion(UserDto userDto, UUID regionId) {
        Region region = regionRepository.findById(regionId)
            .orElseThrow(RegionNotFoundException::new);
        region.activate();
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return keyword.trim();
    }

    private MultiPolygon toMultiPolygon(String wkt) {
        try {
            Geometry g = new WKTReader().read(wkt);
            g.setSRID(4326);
            if (!(g instanceof MultiPolygon)) {
                throw new InvalidRegionGeomException();
            }
            return (MultiPolygon) g;
        } catch (InvalidRegionGeomException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidRegionGeomException();
        }
    }
}