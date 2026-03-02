package com._team._project.domain.store.api.response;

import com._team._project.domain.store.entity.StoreStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class StoreResponse {

    private UUID id;
    private UUID ownerId;
    private UUID regionId;

    private String name;

    private Double latitude;
    private Double longitude;

    private StoreStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
