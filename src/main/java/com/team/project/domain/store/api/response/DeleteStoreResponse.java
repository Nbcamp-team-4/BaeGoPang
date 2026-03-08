package com.team.project.domain.store.api.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class DeleteStoreResponse {
    private UUID id;
    private LocalDateTime deletedAt;
}
