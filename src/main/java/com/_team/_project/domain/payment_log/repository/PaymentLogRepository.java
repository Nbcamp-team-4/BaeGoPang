package com._team._project.domain.payment_log.repository;

import com._team._project.domain.payment_log.entity.PaymentLog;

public interface PaymentLogRepository {
	PaymentLog createPaymentLog(PaymentLog paymentLog);
}
