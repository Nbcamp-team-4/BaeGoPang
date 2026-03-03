package com._team._project.domain.payment_log.service;

import java.util.UUID;

import com._team._project.domain.payment_log.entity.PaymentLog;

public interface PaymentLogService {
	PaymentLog createPaymentLog(PaymentLog paymentLog);

	void deletePaymentLog(UUID paymentLogId);

	PaymentLog getPaymentLog(UUID paymentLogId);
}
