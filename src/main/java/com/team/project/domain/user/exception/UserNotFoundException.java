package com.team.project.domain.user.exception;

import com.team.project.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {
	public UserNotFoundException() {
		super("USER_NOT_FOUND", HttpStatus.NOT_FOUND);
	}
}

