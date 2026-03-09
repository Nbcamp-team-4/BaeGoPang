package com.team.project.global.jwt.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class InvalidTokenException extends BaseException {

	public InvalidTokenException() {
		super("INVALID_TOKEN", HttpStatus.UNAUTHORIZED);
	}
}
