package com.team.project.domain.user.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class UserNotFoundException extends BaseException {
	public UserNotFoundException() {
		super("USER_NOT_FOUND_EXCEPTION", HttpStatus.NOT_FOUND);
	}
}

