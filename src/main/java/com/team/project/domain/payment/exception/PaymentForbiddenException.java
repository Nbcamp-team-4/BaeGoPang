package com.team.project.domain.payment.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class PaymentForbiddenException extends BaseException {
	public PaymentForbiddenException() {
		super("PAYMENT_FORBIDDEN", HttpStatus.FORBIDDEN);
	}
}
