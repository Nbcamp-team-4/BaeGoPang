package com.team.project.domain.product.api.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductRequest {

    @NotNull
    private UUID storeId;

    @NotBlank
    private String name;

    @NotNull
    private Integer price;

    //AI 연동 필요
    private String description;

    @NotNull
    private Boolean useAiDescription;

    private String imageUrl;

    @NotNull
    private UUID userId;
}