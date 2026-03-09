package com.team.project.domain.product.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.product.entity.ProductOptionItem;

/**
 * p_product_option_item 테이블에 대한 기본 CRUD(JPA 제공)
 */
public interface ProductOptionItemRepository extends JpaRepository<ProductOptionItem, UUID> {

	// 옵션 그룹에 속한 옵션 아이템 조회
	List<ProductOptionItem> findAllByProductOptionIdAndDeletedAtIsNull(UUID productOptionId);

	// 옵션 아이템 단건 조회
	Optional<ProductOptionItem> findByIdAndDeletedAtIsNull(UUID optionItemId);
}