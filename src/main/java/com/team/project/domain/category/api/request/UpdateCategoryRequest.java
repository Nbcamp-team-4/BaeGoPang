package com.team.project.domain.category.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCategoryRequest {

    @NotBlank
    @Size(max = 30)
    private String name;
}
