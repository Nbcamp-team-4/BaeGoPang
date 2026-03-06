package com.team.project.domain.payment.repository;

import java.util.Optional;
import java.util.UUID;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;

public interface PaymentRepository {

	/**
	 * 결제 생성
	 */
	Payment createPayment(Payment payment);

	/**
	 * 결제 ID로 결제 단건 조회
	 */
	Optional<Payment> getPayment(UUID paymentId);

	/**
	 * 주문 ID 기준 최신 결제 조회 (삭제 제외)
	 * - 주문 상세/목록 응답에 결제 요약 정보를 붙일 때 사용
	 */
	Optional<Payment> getLatestPaymentByOrderId(UUID orderId);

	/**
	 * 주문 ID 기준 최신 결제 조회 (삭제 포함)
	 * - 결제 재시도/이력 판단용 내부 로직에서 사용
	 */
	Optional<Payment> getLatestPaymentByOrderContainsDeleted(UUID orderId);

	/**
	 * 주문 ID + 결제 상태 기준 최신 결제 조회 (삭제 제외)
	 */
	Optional<Payment> getLatestPaymentByOrderAndStatus(UUID orderId, PaymentStatus paymentStatus);
}