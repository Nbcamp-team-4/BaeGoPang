package com.team.project.domain.user.api.request;


import com.team.project.domain.user.entity.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateStatusRequest {

    @NotNull
    private UserStatus status;
}
