package com.team.project.domain.payment_log.service;

import java.util.UUID;

import com.team.project.domain.payment_log.model.dto.CreatePaymentLogCommand;
import com.team.project.domain.payment_log.model.dto.CreatePaymentQuery;
import com.team.project.domain.payment_log.model.dto.GetPaymentLogQuery;
import com.team.project.domain.payment_log.model.dto.GetPaymentLogsCommand;
import com.team.project.domain.payment_log.model.dto.GetPaymentLogsQuery;

public interface PaymentLogService {
	CreatePaymentQuery createPaymentLog(CreatePaymentLogCommand paymentLogCommand);

	CreatePaymentQuery createPaymentFailureLog(CreatePaymentLogCommand paymentLogCommand);

	GetPaymentLogQuery getPaymentLog(UUID paymentLogId);

	GetPaymentLogsQuery getPaymentLogs(GetPaymentLogsCommand command);

}
