package com.team.project.domain.user.api.response;

import com.team.project.domain.user.entity.RoleType;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserStatus;
import com.team.project.domain.user.model.dto.UserList;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class UserResponse {


    @Schema(description = "유저 ID",format = "uuid")
    private UUID id;
    @Schema(description = "로그인 ID", example = "test1234")
    private String loginId;
    @Schema(description = "email", example = "test1234@naver.com")
    private String email;
    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "번호", example = "010-1111-1111")
    private String phone;
    @Schema(description = "상태", example = "ACTIVE")
    private UserStatus status;
    @Schema(description = "권한", example = "ROLE_CUSTOMER")
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