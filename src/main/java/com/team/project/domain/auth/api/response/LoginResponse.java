package com.team.project.domain.auth.api.response;

import java.util.List;

import com.team.project.domain.user.entity.RoleType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
	private String accessToken;
	private String refreshToken;
	private UserInfo user;

	public LoginResponse(String accessToken, String refreshToken, String loginId, String name, List<RoleType> roles) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.user = new UserInfo(loginId, name, roles);
	}

	@Getter
	@AllArgsConstructor
	public static class UserInfo {
		private String loginId;
		private String name;
		private List<RoleType> roles;
	}
}
