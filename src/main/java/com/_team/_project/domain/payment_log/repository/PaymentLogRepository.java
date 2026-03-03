package com._team._project.domain.payment_log.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com._team._project.domain.payment_log.entity.PaymentLog;
import com._team._project.domain.payment_log.model.vo.PaymentLogStatus;
import com._team._project.global.common.dto.BaseRangeRequest;

public interface PaymentLogRepository {
	PaymentLog createPaymentLog(PaymentLog paymentLog);

	Optional<PaymentLog> getPaymentLog(UUID paymentLogId);

	Page<PaymentLog> getPaymentLogs(PaymentLogStatus status, BaseRangeRequest<LocalDateTime> rangeCreatedAt,
		Pageable pageable);
}
