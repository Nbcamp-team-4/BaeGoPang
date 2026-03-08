package com.team.project.domain.payment.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.global.common.dto.BaseRangeRequest;

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
		Payment save = paymentJpaRepository.save(payment);
		return save;
	}

	/**
	 * 결제 ID로 결제 단건 조회
	 */
	@Override
	public Optional<Payment> getPayment(UUID paymentId) {
		return paymentJpaRepository.findById(paymentId);
	}

	/**
	 * 주문 ID로 최신 결제 조회(삭제 포함)
	 *
	 * 주문 ID를 받아서 삭제된 것을 포함하여 가장 최신 결제를 반환합니다.
	 */
	@Override
	public Optional<Payment> getLatestPaymentByOrderContainsDeleted(UUID orderId) {
		return paymentJpaRepository
			.findLatestByOrderContainsDeleted(orderId)
			.stream()
			.findFirst();
	}

	/**
	 * 주문 ID, 결제 상태로 최신 결제 조회(삭제 제외)
	 *
	 * 주문 ID와 결제 상태를 받아서 조건에 맞는 가장 최신 결제를 반환합니다.
	 */
	@Override
	public Optional<Payment> getLatestPaymentByOrderAndStatus(UUID orderId, PaymentStatus status) {
		return paymentJpaRepository
			.findLatestByOrderAndStatus(orderId, status, PageRequest.of(0, 1))
			.stream()
			.findFirst();
	}

	/**
	 * 주문 ID를 받아서 삭제된 것을 제외하여 가장 최신 결제를 반환합니다.
	 */
	@Override
	public Optional<Payment> getLatestPaymentByOrderId(UUID orderId) {
		return paymentJpaRepository.findLatestByOrder(orderId)
			.stream()
			.findFirst();
	}

	/**
	 * 여러 조건을 받아서 결제 리스트를 조회합니다.
	 */
	@Override
	public Page<Payment> getPayments(PaymentStatus paymentStatus, BaseRangeRequest<Integer> rangeAmount,
		BaseRangeRequest<LocalDateTime> rangePaidAt, UUID orderId, Pageable pageable) {

		return paymentJpaRepository.findPayments(paymentStatus, orderId, rangeAmount.getMin(), rangeAmount.getMax(),
			rangePaidAt.getMin(), rangePaidAt.getMax(), pageable);
	}
}
