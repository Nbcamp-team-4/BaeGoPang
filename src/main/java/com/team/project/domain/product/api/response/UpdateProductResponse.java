package com.team.project.domain.product.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProductResponse {
    private ProductResponse product;
}
