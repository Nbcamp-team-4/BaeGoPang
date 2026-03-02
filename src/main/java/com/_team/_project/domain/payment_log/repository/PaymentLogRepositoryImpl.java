package com._team._project.domain.payment_log.repository;

import org.springframework.stereotype.Repository;

import com._team._project.domain.payment_log.entity.PaymentLog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PaymentLogRepositoryImpl implements PaymentLogRepository {

	private final PaymentLogJpaRepository paymentLogJpaRepository;

	@Override
	public PaymentLog createPaymentLog(PaymentLog paymentLog) {
		return paymentLogJpaRepository.save(paymentLog);
	}
}
