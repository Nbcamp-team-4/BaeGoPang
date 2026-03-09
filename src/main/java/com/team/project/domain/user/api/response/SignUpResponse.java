package com.team.project.domain.user.api.response;

import com.team.project.domain.user.entity.RoleType;
import com.team.project.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class SignUpResponse {

    private UUID id;
    private String loginId;
    private String email;
    private String name;
    private String phone;;
    private RoleType  role;

    public static SignUpResponse from(User user, RoleType role) {
        return SignUpResponse.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .role(role)
                .build();
    }
}
