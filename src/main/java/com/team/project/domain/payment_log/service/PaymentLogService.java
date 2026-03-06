package com.team.project.domain.payment_log.service;

import java.util.UUID;

import com.team.project.domain.payment_log.api.request.GetPaymentLogsRequest;
import com.team.project.domain.payment_log.api.request.GetPaymentLogsResponse;
import com.team.project.domain.payment_log.api.response.GetPaymentLogResponse;
import com.team.project.domain.payment_log.entity.PaymentLog;

public interface PaymentLogService {
	PaymentLog createPaymentLog(PaymentLog paymentLog);

	void deletePaymentLog(UUID paymentLogId);

	GetPaymentLogResponse getPaymentLog(UUID paymentLogId);

	GetPaymentLogsResponse getPaymentLogs(GetPaymentLogsRequest request);
}
