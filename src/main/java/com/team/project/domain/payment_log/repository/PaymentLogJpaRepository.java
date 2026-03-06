package com.team.project.domain.payment_log.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.payment_log.entity.PaymentLog;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;

public interface PaymentLogJpaRepository extends JpaRepository<PaymentLog, UUID> {
	Page<PaymentLog> findByStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(PaymentLogStatus status,
		LocalDateTime start, LocalDateTime end, Pageable pageable);

	Page<PaymentLog> findByCreatedAtGreaterThanEqualAndCreatedAtLessThan(LocalDateTime start, LocalDateTime end,
		Pageable pageable);

	Page<PaymentLog> findByStatus(PaymentLogStatus status, Pageable pageable);
}
