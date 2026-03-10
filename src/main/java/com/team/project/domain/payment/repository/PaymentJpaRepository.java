package com.team.project.domain.payment.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

public interface PaymentJpaRepository extends JpaRepository<Payment, UUID> {

	@Query(value = """
			SELECT *
		    FROM p_payment
			WHERE order_id = :orderId
		    ORDER BY created_at DESC
		    LIMIT 1
		""", nativeQuery = true)
	List<Payment> findLatestByOrderContainsDeleted(@Param("orderId") UUID orderId);

	@Query("""
		    SELECT p
		    FROM Payment p
		    WHERE p.order.id = :orderId AND p.status = :status
		    ORDER BY p.createdAt DESC
		""")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints({
		@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")
	})
	List<Payment> findLatestByOrderAndStatus(@Param("orderId") UUID orderId, @Param("status") PaymentStatus status,
		Pageable pageable);

	@Query("""
			SELECT p
			FROM Payment p
			WHERE p.order.id = :orderId
			ORDER BY p.createdAt DESC
		""")
	List<Payment> findLatestByOrder(UUID orderId);

	@Query("""
		    SELECT p
		    FROM Payment p
		    WHERE (:paymentStatus IS NULL OR p.status = :paymentStatus)
		    AND (:orderId IS NULL OR p.order.id = :orderId)
		    AND (:minAmount IS NULL OR p.amount >= :minAmount)
		    AND (:maxAmount IS NULL OR p.amount <= :maxAmount)
		    AND (:startPaidAt IS NULL OR p.paidAt >= :startPaidAt)
		    AND (:endPaidAt IS NULL OR p.paidAt <= :endPaidAt)
		""")
	Page<Payment> findPayments(
		@Param("paymentStatus") PaymentStatus paymentStatus,
		@Param("orderId") UUID orderId,
		@Param("minAmount") Integer minAmount,
		@Param("maxAmount") Integer maxAmount,
		@Param("startPaidAt") LocalDateTime startPaidAt,
		@Param("endPaidAt") LocalDateTime endPaidAt,
		Pageable pageable
	);
}
