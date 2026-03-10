package com.team.project.domain.product.api.request;

import java.util.UUID;

import com.team.project.domain.product.service.command.CreateProductCommand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProductRequest {

    @NotNull
    private UUID storeId;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotNull
    @PositiveOrZero
    private Integer price;

    // AI 연동 필요
    @Size(max = 50)
    private String description;

    @NotNull
    private Boolean useAiDescription;

    private String imageUrl;

    public CreateProductCommand toCommand() {
        return new CreateProductCommand(
            storeId,
            name,
            price,
            description,
            useAiDescription,
            imageUrl
        );
    }
}