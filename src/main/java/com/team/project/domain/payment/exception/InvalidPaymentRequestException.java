package com.team.project.domain.payment.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class InvalidPaymentRequestException extends BaseException {

	public InvalidPaymentRequestException() {
		super("INVALID_PAYMENT_REQUEST", HttpStatus.BAD_REQUEST);
	}
}
