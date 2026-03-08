package com.team.project.domain.payment.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.global.common.dto.BaseRangeRequest;

public interface PaymentRepository {
	Payment createPayment(Payment payment);

	Optional<Payment> getPayment(UUID paymentId);

	Optional<Payment> getLatestPaymentByOrderContainsDeleted(UUID orderId);

	Optional<Payment> getLatestPaymentByOrderAndStatus(UUID orderId, PaymentStatus paymentStatus);

	Optional<Payment> getLatestPaymentByOrderId(UUID orderId);

	Page<Payment> getPayments(PaymentStatus paymentStatus, BaseRangeRequest<Integer> rangeAmount,
		BaseRangeRequest<LocalDateTime> rangePaidAt, UUID orderId, Pageable pageable);
}
