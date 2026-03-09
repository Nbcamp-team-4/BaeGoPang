package com.team.project.domain.payment.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class PaymentNotFoundException extends BaseException {

	public PaymentNotFoundException() {
		super("PAYMENT_NOT_FOUND", HttpStatus.NOT_FOUND);
	}
}
