package com._team._project.domain.payment.repository;

import com._team._project.domain.payment.entity.Payment;

public interface PaymentRepository {
	Payment createPayment(Payment payment);
}
