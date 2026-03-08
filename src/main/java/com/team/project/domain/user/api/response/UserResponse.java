package com.team.project.domain.user.api.response;

import java.util.List;

import com.team.project.domain.user.entity.UserRole;

import lombok.Getter;

@Getter
public class UserResponse {

	private final Long id;

	private final String loginId;
	private final List<UserRole> roles;

	public UserResponse(Long id, String loginId, List<UserRole> roles) {
		this.id = id;
		this.loginId = loginId;
		this.roles = roles;
	}
}

