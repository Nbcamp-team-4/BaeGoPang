package com.team.project.domain.product.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.product.entity.ProductOption;

/**
 * p_product_option 테이블에 대한 기본 CRUD(JPA 제공)
 */
public interface ProductOptionRepository extends JpaRepository<ProductOption, UUID> {

	// 상품에 속한 옵션 그룹 조회
	List<ProductOption> findAllByProductIdAndDeletedAtIsNull(UUID productId);

	// 옵션 그룹 단건 조회
	Optional<ProductOption> findByIdAndDeletedAtIsNull(UUID optionId);
}