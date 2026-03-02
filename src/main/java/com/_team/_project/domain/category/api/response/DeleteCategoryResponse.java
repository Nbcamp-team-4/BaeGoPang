package com._team._project.domain.category.api.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class DeleteCategoryResponse {
    private UUID id;
    private LocalDateTime deletedAt;
}
