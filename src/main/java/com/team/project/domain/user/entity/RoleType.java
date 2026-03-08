package com.team.project.domain.user.entity;

public enum RoleType {

	CUSTOMER(Authority.CUSTOMER),
	OWNER(Authority.OWNER),
	MANAGER(Authority.MANAGER),
	ADMIN(Authority.ADMIN);

	private final String authority;

	RoleType(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return this.authority;
	}

	public static class Authority {
		public static final String CUSTOMER = "ROLE_CUSTOMER";
		public static final String OWNER = "ROLE_OWNER";
		public static final String MANAGER = "ROLE_MANAGER";
		public static final String ADMIN = "ROLE_ADMIN";
	}
}
