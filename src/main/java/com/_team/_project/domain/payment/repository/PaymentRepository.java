package com._team._project.domain.payment.repository;

import java.util.Optional;
import java.util.UUID;

import com._team._project.domain.payment.entity.Payment;

public interface PaymentRepository {
	Payment createPayment(Payment payment);

	Optional<Payment> getPayment(UUID paymentId);
}
