package com.team.project.domain.user.api.response;

import com.team.project.domain.user.entity.RoleType;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserResponse {

    private UUID id;
    private String loginId;
    private String email;
    private String name;
    private String phone;
    private UserStatus status;
    private RoleType role;

    public static UserResponse from(User user, RoleType role) {
        return UserResponse.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .status(user.getStatus())
                .role(role)
                .build();
    }
}