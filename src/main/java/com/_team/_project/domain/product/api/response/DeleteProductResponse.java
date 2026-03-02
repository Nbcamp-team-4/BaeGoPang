package com._team._project.domain.product.api.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class DeleteProductResponse {
    private UUID id;
    private LocalDateTime deletedAt;
}
