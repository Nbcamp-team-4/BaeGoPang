package com.team.project.domain.pg_provider.exception;

import com.team.project.global.common.exception.BaseException;

public class InvalidInputException extends BaseException {

	public InvalidInputException() {
		super("INVALID_INPUT");
	}
}
