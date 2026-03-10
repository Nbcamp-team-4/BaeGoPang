package com.team.project.domain.payment_log.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.project.domain.payment_log.entity.PaymentLog;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;

public interface PaymentLogJpaRepository extends JpaRepository<PaymentLog, UUID> {
	@Query(
		value = """
			select pl
			from PaymentLog pl
			left join fetch pl.payment p
			where pl.status = :status
			  and pl.createdAt >= :start
			  and pl.createdAt <= :end
			""",
		countQuery = """
			select count(pl)
			from PaymentLog pl
			where pl.status = :status
			  and pl.createdAt >= :start
			  and pl.createdAt <= :end
			"""
	)
	Page<PaymentLog> findByStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
		@Param("status") PaymentLogStatus status,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end,
		Pageable pageable
	);

	@Query(
		value = """
			select pl
			from PaymentLog pl
			left join fetch pl.payment p
			where pl.createdAt >= :start
			  and pl.createdAt <= :end
			""",
		countQuery = """
			select count(pl)
			from PaymentLog pl
			where pl.createdAt >= :start
			  and pl.createdAt <= :end
			"""
	)
	Page<PaymentLog> findByCreatedAtGreaterThanEqualAndCreatedAtLessThan(
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end,
		Pageable pageable
	);

	@Query(
		value = """
			select pl
			from PaymentLog pl
			left join fetch pl.payment p
			where pl.status = :status
			""",
		countQuery = """
			select count(pl)
			from PaymentLog pl
			where pl.status = :status
			"""
	)
	Page<PaymentLog> findByStatus(
		@Param("status") PaymentLogStatus status,
		Pageable pageable
	);

	@Query("select pl from PaymentLog pl where pl.payment.id = :paymentId")
	List<PaymentLog> findAllByPayment(UUID paymentId);
}
