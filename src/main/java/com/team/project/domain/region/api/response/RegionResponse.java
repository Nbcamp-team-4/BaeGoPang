package com.team.project.domain.region.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.region.entity.Region;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegionResponse {

    private UUID id;
    private String name;
    private boolean active;
    private LocalDateTime createdAt;

    public static RegionResponse from(Region region) {
        return new RegionResponse(
                region.getId(),
                region.getName(),
                region.isActive(),   
                region.getCreatedAt()
        );
    }
}