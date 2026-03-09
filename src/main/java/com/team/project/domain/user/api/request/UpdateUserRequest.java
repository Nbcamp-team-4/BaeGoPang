package com.team.project.domain.user.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateUserRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;
}
