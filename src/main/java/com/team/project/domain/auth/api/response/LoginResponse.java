package com.team.project.domain.auth.api.response;

import java.util.List;
import java.util.UUID;

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
	@Schema(description = "유저 정보", example = "loginId = test12, name = testName, roles = ROLE_CUSTOMER ")
	private UserInfo user;

	public LoginResponse(String accessToken, String refreshToken, UUID id, String loginId, String name,
		List<RoleType> roles) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.user = new UserInfo(id, loginId, name, roles);
	}

	@Getter
	@AllArgsConstructor
	public static class UserInfo {
		private UUID id;
		private String loginId;
		private String name;
		private List<RoleType> roles;
	}
}
