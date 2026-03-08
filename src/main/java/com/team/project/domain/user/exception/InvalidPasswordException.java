package com.team.project.domain.user.exception;

public class InvalidPasswordException extends RuntimeException {
	public InvalidPasswordException() {
		super("INVALID_PASSWORD");
	}
}
