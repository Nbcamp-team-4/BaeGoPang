package com.team.project.domain.user.exception;

public class EmailDuplicateException extends RuntimeException {
	public EmailDuplicateException() {
		super("EMAIL_DUPLICATED");
	}
}
