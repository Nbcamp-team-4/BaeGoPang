package com.team.project.domain.auth.api.response;

import com.team.project.domain.address.api.response.SignUpAddressResponse;
import com.team.project.domain.address.dto.CreateUserAddressQuery;
import com.team.project.domain.user.entity.RoleType;
import com.team.project.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class SignUpResponse {
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
    @Schema(description = "권한", example = "ROLE_ADMIN")
    private RoleType  role;

    @Schema(description = "기본 배송지 정보")
    private SignUpAddressResponse address;

    public static SignUpResponse from(User user, RoleType role, CreateUserAddressQuery addressQuery) {
        return SignUpResponse.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .role(role)
                .address(SignUpAddressResponse.from(addressQuery))
                .build();
    }
}
