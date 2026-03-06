package com.team.project.domain.product.api.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HideProductResponse {
    private UUID id;
    private boolean hidden;
}
