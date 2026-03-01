package com._team._project.domain.payment.exception;

import com._team._project.global.common.exception.BaseException;

public class PaymentNotFoundException extends BaseException {

	public PaymentNotFoundException() {
		super("PAYMENT_NOT_FOUND");
	}
}
