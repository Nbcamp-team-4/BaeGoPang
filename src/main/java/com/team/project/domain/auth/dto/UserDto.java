package com.team.project.domain.auth.dto;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {
	private UUID id;
	private String loginId;
	private String name;
	private String email;
	private List<String> roles;

	public static UserDto from(User user) {
		return new UserDto(
			user.getId(),
			user.getLoginId(),
			user.getName(),
			user.getEmail(),
			user.getRoleNames()
		);
	}

	public UserDto(UUID id, String loginId, String name, String email, List<String> roles) {
		this.id = id;
		this.loginId = loginId;
		this.name = name;
		this.email = email;
		this.roles = roles;
	}
}