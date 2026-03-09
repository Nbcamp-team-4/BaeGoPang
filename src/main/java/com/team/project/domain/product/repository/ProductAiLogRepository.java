package com.team.project.domain.product.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.product.entity.ProductAiLog;

public interface ProductAiLogRepository extends JpaRepository<ProductAiLog, UUID> {
}