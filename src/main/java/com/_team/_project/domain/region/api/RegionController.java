package com._team._project.domain.region.api;

import com._team._project.domain.region.api.request.CreateRegionRequest;
import com._team._project.domain.region.api.request.UpdateRegionRequest;
import com._team._project.domain.region.api.response.GetRegionsResponse;
import com._team._project.domain.region.api.response.RegionResponse;
import com._team._project.domain.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService regionService;

    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @PostMapping
    public RegionResponse createRegion(@RequestBody CreateRegionRequest request) {
        return regionService.createRegion(request);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{regionId}")
    public RegionResponse getRegion(@PathVariable UUID regionId) {
        return regionService.getRegion(regionId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public GetRegionsResponse getRegions() {
        return regionService.getRegions();
    }

    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @PatchMapping("/{regionId}")
    public RegionResponse updateRegion(@PathVariable UUID regionId,
                                       @RequestBody UpdateRegionRequest request) {
        return regionService.updateRegion(regionId, request);
    }

    @PreAuthorize("hasAnyRole()('MASTER','MANAGER')")
    @PatchMapping("/{regionId}/deactivate")
    public void deactivateRegion(@PathVariable UUID regionId) {
        regionService.deactivateRegion(regionId);
    }
}