package com._team._project.domain.payment.exception;

import com._team._project.global.common.exception.BaseException;

public class InvalidPaymentMethodException extends BaseException {

	public InvalidPaymentMethodException() {
		super("INVALID_PAYMENT_METHOD_EXCEPTION");
	}
}
