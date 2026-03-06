package com.team.project.domain.product.api.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest {

    @NotNull
    private UUID userId;

    private String name;
    private Integer price;
    private String description;
    private Boolean useAiDescription;
    private String imageUrl;
    private Boolean isSoldOut;
    private Boolean isHidden;

}
