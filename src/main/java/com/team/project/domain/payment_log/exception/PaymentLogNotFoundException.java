package com.team.project.domain.payment_log.exception;

import com.team.project.global.common.exception.BaseException;

public class PaymentLogNotFoundException extends BaseException {

	public PaymentLogNotFoundException() {
		super("PAYMENT_LOG_NOT_FOUND");
	}
}
