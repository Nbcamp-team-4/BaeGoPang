package com.team.project.domain.payment.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class PaymentAmountMismatchException extends BaseException {
	public PaymentAmountMismatchException() {
		super("PAYMENT_AMOUNT_MISMATCH", HttpStatus.BAD_REQUEST);
	}
}
