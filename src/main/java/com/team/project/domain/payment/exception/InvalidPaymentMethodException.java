package com.team.project.domain.payment.exception;

import com.team.project.global.common.exception.BaseException;

public class InvalidPaymentMethodException extends BaseException {

	public InvalidPaymentMethodException() {
		super("INVALID_PAYMENT_METHOD_EXCEPTION");
	}
}
