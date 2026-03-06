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

	/**
	 * 주문 ID 기준 최신 결제 조회 (삭제 제외)
	 * - 엔티티의 @Where(clause = "deleted_at IS NULL") 영향으로
	 *   JPQL 조회 시 soft delete 데이터는 자동 제외된다.
	 */
	@Query("""
		    SELECT p
		    FROM Payment p
		    WHERE p.order.id = :orderId
		    ORDER BY p.createdAt DESC
		""")
	List<Payment> findLatestByOrder(@Param("orderId") UUID orderId, Pageable pageable);

	/**
	 * 주문 ID 기준 최신 결제 조회 (삭제 포함)
	 * - native query를 사용하여 soft delete 데이터도 포함해서 조회
	 */
	@Query(value = """
			SELECT *
		    FROM p_payment
			WHERE order_id = :orderId
		    ORDER BY created_at DESC
		    LIMIT 1
		""", nativeQuery = true)
	List<Payment> findLatestByOrderContainsDeleted(@Param("orderId") UUID orderId);

	/**
	 * 주문 ID + 상태 기준 최신 결제 조회 (삭제 제외)
	 */
	@Query("""
		    SELECT p
		    FROM Payment p
		    WHERE p.order.id = :orderId
		      AND p.status = :status
		    ORDER BY p.createdAt DESC
		""")
	List<Payment> findLatestByOrderAndStatus(
			@Param("orderId") UUID orderId,
			@Param("status") PaymentStatus status,
			Pageable pageable
	);
}