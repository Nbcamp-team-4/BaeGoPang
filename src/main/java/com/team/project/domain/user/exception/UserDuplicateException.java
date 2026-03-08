package com.team.project.domain.user.exception;

public class UserDuplicateException extends RuntimeException {
	public UserDuplicateException() {
		super("USER_DUPLICATED");
	}
}
