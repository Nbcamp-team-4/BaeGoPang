package com.team.project.domain.auth.api.request;

import com.team.project.domain.user.entity.RoleType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
	private String loginId;
	private String password;
	private String name;
	private String email;
	private String phone;
	private RoleType type;
}