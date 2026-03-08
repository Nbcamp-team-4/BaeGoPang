package com.team.project.domain.payment_log.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class InvalidInputException extends BaseException {

	public InvalidInputException() {
		super("INVALID_INPUT", HttpStatus.BAD_REQUEST);
	}
}
