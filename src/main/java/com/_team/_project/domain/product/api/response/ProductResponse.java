package com._team._project.domain.product.api.response;

import com._team._project.domain.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ProductResponse {

    private UUID id;
    private UUID storeId;
    private UUID categoryId;

    private String name;
    private BigDecimal price;
    private String description;

    private boolean hidden;
    private ProductStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
