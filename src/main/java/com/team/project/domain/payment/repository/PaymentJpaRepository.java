package com.team.project.domain.payment.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;

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
	List<Payment> findLatestByOrderAndStatus(@Param("orderId") UUID orderId, @Param("status") PaymentStatus status,
		Pageable pageable);

	@Query("""
			SELECT p
			FROM Payment p
			WHERE p.order.id = :orderId
			ORDER BY p.createdAt DESC
		""")
	List<Payment> findLatestByOrder(UUID orderId);
}
