package com.team.project.domain.region.api;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.region.api.request.CreateRegionRequest;
import com.team.project.domain.region.api.request.RegionSearchRequest;
import com.team.project.domain.region.api.request.UpdateRegionRequest;
import com.team.project.domain.region.api.response.PagedRegionsResponse;
import com.team.project.domain.region.api.response.RegionResponse;
import com.team.project.domain.region.service.RegionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Region", description = "지역 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/regions")
@Validated
public class RegionController {

    private final RegionService regionService;

    @Operation(summary = "지역 생성", description = "관리자가 지역을 생성합니다.")
    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    public RegionResponse createRegion(@Valid @RequestBody CreateRegionRequest request) {
        UserDto userDto = null; // TODO: Security 적용 후 주입
        return regionService.createRegion(userDto, request);
    }

    @Operation(summary = "지역 조회", description = "특정 지역 정보를 조회합니다.")
    @GetMapping("/{regionId}")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    public RegionResponse getRegion(@PathVariable UUID regionId) {
        return regionService.getRegion(regionId);
    }

    @Operation(summary = "지역 목록 조회 (사용자)", description = "사용자가 활성화된 지역 목록을 조회합니다.")
    @GetMapping
    public PagedRegionsResponse getRegionsForUser(@ModelAttribute RegionSearchRequest request) {
        return regionService.getRegionsForUser(request);
    }

    @Operation(summary = "지역 목록 조회 (관리자)", description = "관리자가 전체 지역 목록을 조회합니다.")
    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    public PagedRegionsResponse getRegionsForAdmin(@ModelAttribute RegionSearchRequest request) {
        return regionService.getRegionsForAdmin(request);
    }

    @Operation(summary = "지역 수정", description = "관리자가 지역 정보를 수정합니다.")
    @PatchMapping("/{regionId}")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    public RegionResponse updateRegion(
        @PathVariable UUID regionId,
        @Valid @RequestBody UpdateRegionRequest request
    ) {
        UserDto userDto = null; // TODO: Security 적용 후 주입
        return regionService.updateRegion(userDto, regionId, request);
    }

    @Operation(summary = "지역 비활성화", description = "관리자가 지역을 비활성화합니다.")
    @PatchMapping("/{regionId}/deactivate")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    public void deactivateRegion(@PathVariable UUID regionId) {
        UserDto userDto = null; // TODO: Security 적용 후 주입
        regionService.deactivateRegion(userDto, regionId);
    }

    @Operation(summary = "지역 활성화", description = "관리자가 지역을 활성화합니다.")
    @PatchMapping("/{regionId}/activate")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    public void activateRegion(@PathVariable UUID regionId) {
        UserDto userDto = null; // TODO: Security 적용 후 주입
        regionService.activateRegion(userDto, regionId);
    }
}