package com.team.project.domain.payment.repository;

import java.util.Optional;
import java.util.UUID;

import com.team.project.domain.payment.entity.Payment;

public interface PaymentRepository {
	Payment createPayment(Payment payment);

	Optional<Payment> getPayment(UUID paymentId);
}
