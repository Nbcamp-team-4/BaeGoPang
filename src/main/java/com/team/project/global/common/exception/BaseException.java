package com.team.project.global.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

	protected String errorCode;
	protected HttpStatus httpStatus;

	public BaseException(String errorCode, HttpStatus httpStatus) {
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
	}
}
