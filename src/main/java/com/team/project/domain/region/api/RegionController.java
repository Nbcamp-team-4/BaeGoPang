package com.team.project.domain.region.api;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.region.api.request.CreateRegionRequest;
import com.team.project.domain.region.api.request.UpdateRegionRequest;
import com.team.project.domain.region.api.response.PagedRegionsResponse;
import com.team.project.domain.region.api.response.RegionResponse;
import com.team.project.domain.region.service.RegionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/regions")
@Validated
public class RegionController {

    private final RegionService regionService;

    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @PostMapping
    public RegionResponse createRegion(@Valid @RequestBody CreateRegionRequest request) {
        return regionService.createRegion(request);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{regionId}")
    public RegionResponse getRegion(@PathVariable UUID regionId) {
        return regionService.getRegion(regionId);
    }

    // 사용자: 활성만
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public PagedRegionsResponse getRegionsForUser(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return regionService.getRegionsForUser(pageable);
    }

    // 관리자: 전체
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @GetMapping("/admin")
    public PagedRegionsResponse getRegionsForAdmin(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return regionService.getRegionsForAdmin(pageable);
    }

    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @PatchMapping("/{regionId}")
    public RegionResponse updateRegion(@PathVariable UUID regionId,
                                       @Valid @RequestBody UpdateRegionRequest request) {
        return regionService.updateRegion(regionId, request);
    }

    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @PatchMapping("/{regionId}/deactivate")
    public void deactivateRegion(@PathVariable UUID regionId) {
        regionService.deactivateRegion(regionId);
    }

    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @PatchMapping("/{regionId}/activate")
    public void activateRegion(@PathVariable UUID regionId) {
        regionService.activateRegion(regionId);
    }
}