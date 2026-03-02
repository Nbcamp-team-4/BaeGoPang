package com._team._project.domain.product.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateProductResponse {
    private ProductResponse product;
}
