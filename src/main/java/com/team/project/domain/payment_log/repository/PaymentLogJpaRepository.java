package com.team.project.domain.payment_log.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.team.project.domain.payment_log.entity.PaymentLog;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;

public interface PaymentLogJpaRepository extends JpaRepository<PaymentLog, UUID> {
	Page<PaymentLog> findByStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(PaymentLogStatus status,
		LocalDateTime start, LocalDateTime end, Pageable pageable);

	Page<PaymentLog> findByCreatedAtGreaterThanEqualAndCreatedAtLessThan(LocalDateTime start, LocalDateTime end,
		Pageable pageable);

	Page<PaymentLog> findByStatus(PaymentLogStatus status, Pageable pageable);

	@Query("select pl from PaymentLog pl where pl.payment.id = :paymentId")
	List<PaymentLog> findAllByPayment(UUID paymentId);
}
