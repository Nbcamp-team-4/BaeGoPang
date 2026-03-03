package com._team._project.domain.payment.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com._team._project.domain.payment.entity.Payment;

public interface PaymentJpaRepository extends JpaRepository<Payment, UUID> {
}
