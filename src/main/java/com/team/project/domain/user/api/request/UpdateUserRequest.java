package com.team.project.domain.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateUserRequest {

    @Email(message = "올바른 이메일 형식이어야 합니다.")
    String email;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;
}
