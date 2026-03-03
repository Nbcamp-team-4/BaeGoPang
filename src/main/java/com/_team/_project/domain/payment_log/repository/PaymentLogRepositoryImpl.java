package com._team._project.domain.payment_log.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com._team._project.domain.payment_log.entity.PaymentLog;
import com._team._project.domain.payment_log.exception.InvalidInputException;
import com._team._project.domain.payment_log.model.vo.PaymentLogStatus;
import com._team._project.global.common.dto.BaseRangeRequest;

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

	@Override
	public Optional<PaymentLog> getPaymentLog(UUID paymentLogId) {
		return paymentLogJpaRepository.findById(paymentLogId);
	}

	@Override
	public Page<PaymentLog> getPaymentLogs(PaymentLogStatus status, BaseRangeRequest<LocalDateTime> rangeCreatedAt,
		Pageable pageable) {
		LocalDateTime start = null;
		LocalDateTime end = null;

		if (rangeCreatedAt != null) {
			if (rangeCreatedAt.getMax() == null || rangeCreatedAt.getMin() == null) {
				throw new InvalidInputException();
			}
			start = rangeCreatedAt.getMin();
			end = rangeCreatedAt.getMax();
		}

		// 1. status + range 둘 다 있는 경우
		if (status != null && start != null && end != null) {
			return paymentLogJpaRepository
				.findByStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
					status, start, end, pageable
				);
		}

		// 2. range만 있는 경우
		if (status == null && start != null && end != null) {
			return paymentLogJpaRepository
				.findByCreatedAtGreaterThanEqualAndCreatedAtLessThan(
					start, end, pageable
				);
		}

		// 3. status만 있는 경우
		if (status != null) {
			return paymentLogJpaRepository.findByStatus(status, pageable);
		}

		// 4. 아무 조건도 없으면 전체 조회
		return paymentLogJpaRepository.findAll(pageable);
	}
}
