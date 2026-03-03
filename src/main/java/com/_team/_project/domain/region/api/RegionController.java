package com._team._project.domain.region.api;

import com._team._project.domain.region.api.request.CreateRegionRequest;
import com._team._project.domain.region.api.request.UpdateRegionRequest;
import com._team._project.domain.region.api.response.GetRegionsResponse;
import com._team._project.domain.region.api.response.RegionResponse;
import com._team._project.domain.region.service.RegionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/regions")
@Validated
public class RegionController {

    private final RegionService regionService;

    /*
     * 지역 등록
     */
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @PostMapping
    public RegionResponse createRegion(@Valid @RequestBody CreateRegionRequest request) {
        return regionService.createRegion(request);
    }


    /*
     * 지역 세부 사항 조회
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{regionId}")
    public RegionResponse getRegion(@PathVariable UUID regionId) {
        return regionService.getRegion(regionId);
    }

    /*
     * 지역 목록 조회
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public GetRegionsResponse getRegions() {
        return regionService.getRegions();
    }

    /*
     * 지역 수정
     */
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @PatchMapping("/{regionId}")
    public RegionResponse updateRegion(@PathVariable UUID regionId,
                                       @Valid @RequestBody UpdateRegionRequest request) {
        return regionService.updateRegion(regionId, request);
    }

    /*
     * 지역 활성화/비활성화 여부
     */
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @PatchMapping("/{regionId}/deactivate")
    public void deactivateRegion(@PathVariable UUID regionId) {
        regionService.deactivateRegion(regionId);
    }
}
