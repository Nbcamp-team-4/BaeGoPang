package com.team.project.domain.user.exception;

import com.team.project.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserDuplicateException extends BaseException {
	public UserDuplicateException() {
		super("USER_DUPLICATED", HttpStatus.BAD_REQUEST);
	}
}
