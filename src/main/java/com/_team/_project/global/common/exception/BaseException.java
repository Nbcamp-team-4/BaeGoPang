package com._team._project.global.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

	protected String errorCode;

	public BaseException(String errorCode) {
		this.errorCode = errorCode;
	}
}
