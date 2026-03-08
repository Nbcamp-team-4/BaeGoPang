package com._team._project.domain.user.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;
@Data
public class LoginUserRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private String loginId;

    @NotNull
    private String password;

}
