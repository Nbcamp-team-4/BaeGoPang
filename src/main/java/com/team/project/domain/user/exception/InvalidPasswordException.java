package com.team.project.domain.user.exception;

import com.team.project.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends BaseException {
	public InvalidPasswordException() {
		super("INVALID_PASSWORD", HttpStatus.BAD_REQUEST);
	}
}
