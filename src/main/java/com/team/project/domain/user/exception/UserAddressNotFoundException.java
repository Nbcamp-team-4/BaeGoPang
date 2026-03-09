package com.team.project.domain.user.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class UserAddressNotFoundException extends BaseException {
	public UserAddressNotFoundException() {
		super("USER_ADDRESS_NOT_FOUND", HttpStatus.NOT_FOUND);
	}
}
