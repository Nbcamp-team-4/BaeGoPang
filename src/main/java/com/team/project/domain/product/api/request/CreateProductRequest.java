package com.team.project.domain.product.api.request;

import java.util.UUID;

import com.team.project.domain.product.service.command.CreateProductCommand;

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