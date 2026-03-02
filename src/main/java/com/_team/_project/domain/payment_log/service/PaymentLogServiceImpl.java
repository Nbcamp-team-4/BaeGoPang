package com._team._project.domain.payment_log.service;

import org.springframework.stereotype.Service;

import com._team._project.domain.payment_log.entity.PaymentLog;
import com._team._project.domain.payment_log.repository.PaymentLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentLogServiceImpl implements PaymentLogService {

	private final PaymentLogRepository paymentLogRepository;

	@Override
	public PaymentLog createPaymentLog(PaymentLog paymentLog) {
		PaymentLog saved = paymentLogRepository.createPaymentLog(paymentLog);
		return saved;
	}
}
