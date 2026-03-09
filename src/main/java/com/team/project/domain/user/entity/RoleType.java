package com.team.project.domain.user.entity;

import lombok.Getter;

@Getter
public enum RoleType {

	ROLE_CUSTOMER("ROLE_CUSTOMER"),
	ROLE_OWNER("ROLE_OWNER"),
	ROLE_MANAGER("ROLE_MANAGER"),
	ROLE_ADMIN("ROLE_ADMIN");


	private final String authority;

	RoleType(String authority) {
		this.authority = authority;
	}

}
