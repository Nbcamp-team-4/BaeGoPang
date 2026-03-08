package com.team.project.domain.product.api.request;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.product.service.command.UpdateProductCommand;

import lombok.Data;

@Data
public class UpdateProductRequest {

    private String name;
    private Integer price;
    private String description;
    private Boolean useAiDescription;
    private String imageUrl;

    private List<UpdateOptionGroupRequest> options;

    public UpdateProductCommand toCommand(UUID productId) {
        return UpdateProductCommand.builder()
            .productId(productId)
            .name(name)
            .price(price)
            .description(description)
            .useAiDescription(useAiDescription)
            .imageUrl(imageUrl)
            .options(
                options == null ? List.of() : options.stream()
                    .map(UpdateOptionGroupRequest::toCommand)
                    .toList()
            )
            .build();
    }
}