package com._team._project.domain.product.api.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class HideProductResponse {
    private UUID id;
    private boolean hidden;
}
