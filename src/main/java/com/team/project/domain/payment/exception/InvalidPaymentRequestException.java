package com.team.project.domain.payment.exception;

import com.team.project.global.common.exception.BaseException;

public class InvalidPaymentRequestException extends BaseException {

	public InvalidPaymentRequestException() {
		super("INVALID_PAYMENT_REQUEST");
	}
}
