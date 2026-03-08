package com.team.project.domain.user.api.response;

import com.team.project.domain.user.entity.UserRole;

import lombok.Getter;

@Getter
public class UserResponse {

	private final Long id;

	private final String loginId;
	private final UserRole role;

	private final List<UserRole> roles;

	public UserResponse(Long id, String loginId, UserRole role) {
		this.id = id;
		this.loginId = loginId;
		this.role = role;
		this.role = role;
	}
}

