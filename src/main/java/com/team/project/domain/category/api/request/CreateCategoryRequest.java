package com.team.project.domain.category.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryRequest {

    @NotNull
    @Size(max = 30)
    private String name;
}
