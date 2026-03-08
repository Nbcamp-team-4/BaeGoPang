package com.team.project.domain.product.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;

@Getter
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
    private LocalDateTime createdAt;

    public ProductResponse(
        UUID id,
        UUID storeId,
        String name,
        Integer price,
        String description,
        Boolean useAiDescription,
        String imageUrl,
        Boolean isSoldOut,
        Boolean isHidden,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.id = id;
        this.storeId = storeId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.useAiDescription = useAiDescription;
        this.imageUrl = imageUrl;
        this.isSoldOut = isSoldOut;
        this.isHidden = isHidden;
        this.createdAt = createdAt;
    }
}