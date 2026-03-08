package com.team.project.domain.product.api.request;

import java.util.UUID;

import com.team.project.domain.product.service.command.UpdateProductCommand;

import lombok.Data;

@Data
public class UpdateProductRequest {

    private String name;

    private Integer price;

    // AI 연동 필요
    private String description;

    private Boolean useAiDescription;

    private String imageUrl;

    public UpdateProductCommand toCommand(UUID productId) {
        return new UpdateProductCommand(
            productId,
            name,
            price,
            description,
            useAiDescription,
            imageUrl
        );
    }
}