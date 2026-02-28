package com._team._project.domain.region.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateRegionResponse {

    private UUID id;
    private String name;
    private Boolean isActive;
    private LocalDateTime createdAt;
}