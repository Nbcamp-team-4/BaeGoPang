package com.team.project.domain.payment.exception;

import com.team.project.global.common.exception.BaseException;

public class PaymentAlreadyPaidException extends BaseException {

	public PaymentAlreadyPaidException() {
		super("PAYMENT_AREADY_PAID");
	}
}
