package com._team._project.domain.payment.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com._team._project.domain.payment.entity.Payment;

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
}
