package com.team.project.domain.product.api.response;

import java.util.UUID;

import com.team.project.domain.product.service.result.ProductResult;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {

    private UUID id;
    private UUID storeId;
    private String name;
    private Integer price;
    private String description;
    private Boolean useAiDescription;
    private String imageUrl;
    private Boolean isSoldOut;
    private Boolean isHidden;

    public static ProductResponse from(ProductResult result) {
        return ProductResponse.builder()
            .id(result.getId())
            .storeId(result.getStoreId())
            .name(result.getName())
            .price(result.getPrice())
            .description(result.getDescription())
            .useAiDescription(result.getUseAiDescription())
            .imageUrl(result.getImageUrl())
            .isSoldOut(result.getIsSoldOut())
            .isHidden(result.getIsHidden())
            .build();
    }
}