package com._team._project.domain.product.api.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetProductsResponse {
    private List<ProductResponse> products;
}
