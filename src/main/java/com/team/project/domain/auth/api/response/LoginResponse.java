package com.team.project.domain.auth.api.response;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.user.entity.RoleType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
	private String accessToken;
	private String refreshToken;
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
