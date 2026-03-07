package com.team.project.domain.payment.repository;

import java.util.Optional;
import java.util.UUID;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;

public interface PaymentRepository {
	Payment createPayment(Payment payment);

	Optional<Payment> getPayment(UUID paymentId);

	Optional<Payment> getLatestPaymentByOrderContainsDeleted(UUID orderId);

	Optional<Payment> getLatestPaymentByOrderAndStatus(UUID orderId, PaymentStatus paymentStatus);
}
