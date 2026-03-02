package com._team._project.domain.product.api.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreateProductRequest {

    private UUID storeId;
    private UUID categoryId;

    private String name;
    private BigDecimal price;

    // AI 생성/프롬프트 AI 담당자 영역
    private String description;
}
