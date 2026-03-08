package com.team.project.domain.store.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteStoreResponse {
    private UUID id;
    private LocalDateTime deletedAt;
}
