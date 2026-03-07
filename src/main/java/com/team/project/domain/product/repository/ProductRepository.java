package com.team.project.domain.product.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.project.domain.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

	List<Product> findAllByStoreIdAndDeletedAtIsNull(UUID storeId);

	List<Product> findAllByStoreIdAndDeletedAtIsNullAndIsHiddenFalse(UUID storeId);

	@Modifying(clearAutomatically = true) // UPDATE 쿼리 필수 어노테이션
	@Query("UPDATE Product p SET p.deletedAt = CURRENT_TIMESTAMP, p.deletedBy = :userId WHERE p.store.id = :storeId AND p.deletedAt IS NULL")
	void softDeleteByStoreId(@Param("storeId") UUID storeId, @Param("userId") UUID userId);

	// 목록/상세 조회 시 deletedAt IS NULL 조건 필요함
	// Search(Page/Slice) + 정렬 + size 제한 필요함
	// Store/Category 조건 검색 필요함
}
