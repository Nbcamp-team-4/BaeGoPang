package com.team.project.domain.user.api.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class UserRequest {
    @NotNull(message = "userId는 필수입니다.")
    UUID userId;
}