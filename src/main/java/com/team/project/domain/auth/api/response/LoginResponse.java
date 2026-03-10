package com.team.project.domain.auth.api.response;

import java.util.List;

import com.team.project.domain.user.entity.RoleType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
	@Schema(description = "accessToken", example = "eyJhbGciOiJIUzI1NiJ9")
	private String accessToken;
	@Schema(description = "refreshToken", example = "eyJzdWIiOiJqeXk3MTRAbmF2ZXIuY29tIiwiaWF0IjoxNzM4Mzk3NTI0LCJleHAiOjE3MzkwMDIzMjR9")
	private String refreshToken;
	@Schema(description = "유저 정보")
	private UserInfo user;

	public LoginResponse(String accessToken, String refreshToken, String loginId, String name, List<RoleType> roles) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.user = new UserInfo(loginId, name, roles);
	}

	@Getter
	@AllArgsConstructor
	public static class UserInfo {

		@Schema(description = "로그인 ID", example = "testid12")
		private String loginId;
		@Schema(description = "이름", example = "홍길동")
		private String name;
		@Schema(description = "권한 목록", example = "[ROLE_CUSTOMER]")
		private List<RoleType> roles;
	}
}
