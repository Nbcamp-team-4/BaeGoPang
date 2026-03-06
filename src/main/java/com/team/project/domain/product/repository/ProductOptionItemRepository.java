package com.team.project.domain.product.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.product.entity.ProductOptionItem;

/**
 * p_product_option_item 테이블에 대한 기본 CRUD(JPA 제공)
 */
public interface ProductOptionItemRepository extends JpaRepository<ProductOptionItem, UUID> {
}