package com._team._project.domain.product.api.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class UpdateProductRequest {

    private UUID categoryId;

    private String name;
    private BigDecimal price;

    private String description;
}
