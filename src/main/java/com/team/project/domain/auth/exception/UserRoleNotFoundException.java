package com.team.project.domain.auth.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class UserRoleNotFoundException extends BaseException {
	public UserRoleNotFoundException() {
		super("USER_SHOUD_HAVE_ROLE", HttpStatus.FORBIDDEN);
	}
}
