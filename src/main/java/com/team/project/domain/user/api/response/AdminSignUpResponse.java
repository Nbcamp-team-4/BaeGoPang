package com.team.project.domain.user.api.response;

import com.team.project.domain.user.entity.RoleType;
import com.team.project.domain.user.entity.User;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AdminSignUpResponse {
    private UUID id;
    private String loginId;
    private String email;
    private String name;
    private String phone;;

    public static AdminSignUpResponse from(User user) {
        return SignUpResponse.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .build();
    }
}
