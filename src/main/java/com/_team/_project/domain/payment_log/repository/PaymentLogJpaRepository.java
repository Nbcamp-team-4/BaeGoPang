package com._team._project.domain.payment_log.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com._team._project.domain.payment_log.entity.PaymentLog;

public interface PaymentLogJpaRepository extends JpaRepository<PaymentLog, UUID> {
}
