package com.team.project.domain.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Email(message = "올바른 이메일 형식이어야 합니다.")
    String email;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;
}
