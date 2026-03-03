package com._team._project.domain.category.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateCategoryRequest {

    @NotNull
    @Size(max = 30)
    private String name;
}
