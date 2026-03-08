package com.team.project.domain.user.exception;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException() {
		super("USER_NOT_FOUND");
	}
}

