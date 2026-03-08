package com.team.project.domain.user.api.response;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserRole;

import com.team.project.domain.user.entity.UserStatus;
import lombok.Getter;

@Getter
public class UserResponse {

	private UUID id;
	private String loginId;
	private String email;
	private String name;
	private String phone;
	private UserStatus status;

	private List<String> roles;

	public UserResponse(User user) {
		this.id = user.getId();
		this.loginId = user.getLoginId();
		this.email = user.getEmail();
		this.name = user.getName();
		this.phone = user.getPhone();
		this.status = user.getStatus();

		this.roles = user.getUserRoles().stream()
				.map(userRole -> userRole.getRole().getRole().name())
				.toList();
	}
}

