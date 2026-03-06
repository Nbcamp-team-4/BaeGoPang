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

	@Override
	public Payment createPayment(Payment payment) {
		Payment save = paymentJpaRepository.save(payment);
		return save;
	}

	@Override
	public Optional<Payment> getPayment(UUID paymentId) {
		return paymentJpaRepository.findById(paymentId);
	}

	@Override
	public Optional<Payment> getLatestPaymentByOrderContainsDeleted(UUID orderId) {
		return paymentJpaRepository
			.findLatestByOrderContainsDeleted(orderId)
			.stream()
			.findFirst();
	}

	@Override
	public Optional<Payment> getLatestPaymentByOrderAndStatus(UUID orderId, PaymentStatus status) {
		return paymentJpaRepository
			.findLatestByOrderAndStatus(orderId, status, PageRequest.of(0, 1))
			.stream()
			.findFirst();
	}
}
