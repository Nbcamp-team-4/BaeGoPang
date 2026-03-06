package com.team.project.domain.product.api.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetProductsResponse {
    private List<ProductResponse> products;
}
