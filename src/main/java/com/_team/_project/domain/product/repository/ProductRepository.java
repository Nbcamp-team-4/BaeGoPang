package com._team._project.domain.product.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com._team._project.domain.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

	List<Product> findAllByStoreIdAndDeletedAtIsNull(UUID storeId);
	List<Product> findAllByStoreIdAndDeletedAtIsNullAndIsHiddenFalse(UUID storeId);

	// 목록/상세 조회 시 deletedAt IS NULL 조건 필요함
    // Search(Page/Slice) + 정렬 + size 제한 필요함
    // Store/Category 조건 검색 필요함
}
