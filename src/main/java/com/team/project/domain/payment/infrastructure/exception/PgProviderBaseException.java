package com.team.project.domain.payment.infrastructure.exception;

import com.team.project.global.common.exception.BaseException;

public class PgProviderBaseException extends BaseException {
	private String message;

	public PgProviderBaseException(String errorCode, String message) {
		super(errorCode);
		this.message = message;
	}
}
