package com.team.project.domain.user.api.response;

import com.team.project.domain.user.entity.RoleType;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserStatus;
import com.team.project.domain.user.model.dto.UserList;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
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
    private List<RoleType> roles;

    public static UserResponse from(User result) {
        return from(result, List.of());
    }

    public static UserResponse from(User user, List<RoleType> roles) {
        return UserResponse.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .status(user.getStatus())
                .roles(roles)
                .build();
    }
}