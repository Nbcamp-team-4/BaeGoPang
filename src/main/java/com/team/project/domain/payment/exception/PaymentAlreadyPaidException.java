package com.team.project.domain.payment.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class PaymentAlreadyPaidException extends BaseException {

	public PaymentAlreadyPaidException() {
		super("PAYMENT_AREADY_PAID", HttpStatus.BAD_REQUEST);
	}
}
