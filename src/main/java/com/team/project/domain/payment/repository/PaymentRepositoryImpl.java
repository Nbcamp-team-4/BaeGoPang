package com.team.project.domain.payment.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

	private final PaymentJpaRepository paymentJpaRepository;

	/**
	 * 결제 생성
	 */
	@Override
	public Payment createPayment(Payment payment) {
		return paymentJpaRepository.save(payment);
	}

	/**
	 * 결제 ID로 결제 단건 조회
	 */
	@Override
	public Optional<Payment> getPayment(UUID paymentId) {
		return paymentJpaRepository.findById(paymentId);
	}

	/**
	 * 주문 ID 기준 최신 결제 조회 (삭제 제외)
	 * - 주문 응답에 paymentId, paymentStatus를 붙일 때 사용
	 */
	@Override
	public Optional<Payment> getLatestPaymentByOrderId(UUID orderId) {
		return paymentJpaRepository
				.findLatestByOrder(orderId, PageRequest.of(0, 1))
				.stream()
				.findFirst();
	}

	/**
	 * 주문 ID 기준 최신 결제 조회 (삭제 포함)
	 * - 기존 결제 이력까지 포함해서 가장 최신 결제를 확인할 때 사용
	 */
	@Override
	public Optional<Payment> getLatestPaymentByOrderContainsDeleted(UUID orderId) {
		return paymentJpaRepository
				.findLatestByOrderContainsDeleted(orderId)
				.stream()
				.findFirst();
	}

	/**
	 * 주문 ID + 결제 상태 기준 최신 결제 조회 (삭제 제외)
	 */
	@Override
	public Optional<Payment> getLatestPaymentByOrderAndStatus(UUID orderId, PaymentStatus status) {
		return paymentJpaRepository
				.findLatestByOrderAndStatus(orderId, status, PageRequest.of(0, 1))
				.stream()
				.findFirst();
	}
}