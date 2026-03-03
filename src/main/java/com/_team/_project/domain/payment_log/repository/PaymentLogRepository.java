package com._team._project.domain.payment_log.repository;

import java.util.Optional;
import java.util.UUID;

import com._team._project.domain.payment_log.entity.PaymentLog;

public interface PaymentLogRepository {
	PaymentLog createPaymentLog(PaymentLog paymentLog);

	Optional<PaymentLog> getPaymentLog(UUID paymentLogId);
}
