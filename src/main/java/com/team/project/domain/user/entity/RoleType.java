package com.team.project.domain.user.entity;

public enum RoleType {

	CUSTOMER("ROLE_CUSTOMER"),
	OWNER("ROLE_OWNER"),
	MANAGER("ROLE_MANAGER"),
	ADMIN("ROLE_ADMIN");


	private final String authority;

	RoleType(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return authority;
	}
}
