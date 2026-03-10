package com.team.project.domain.product.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.project.domain.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

	// ===== 목록 조회 =====

	// 관리자/점주용: 숨김, 품절 포함 (삭제만 제외)
	List<Product> findAllByStoreIdAndDeletedAtIsNull(UUID storeId);

	// 사용자용: 노출 상품 + 품절 제외
	List<Product> findAllByStoreIdAndDeletedAtIsNullAndIsHiddenFalseAndIsSoldOutFalse(UUID storeId);


	// ===== 단건 조회 =====

	// 관리자/점주용
	Optional<Product> findByIdAndDeletedAtIsNull(UUID productId);

	// 사용자용
	Optional<Product> findByIdAndDeletedAtIsNullAndIsHiddenFalseAndIsSoldOutFalse(UUID productId);


	// ===== 소프트 삭제 =====

	@Modifying(clearAutomatically = true)
	@Query("""
        UPDATE Product p
           SET p.deletedAt = CURRENT_TIMESTAMP,
               p.deletedBy = :userId
         WHERE p.store.id = :storeId
           AND p.deletedAt IS NULL
    """)
	void softDeleteByStoreId(@Param("storeId") UUID storeId, @Param("userId") UUID userId);

	//AI가 조회시 필요한쿼리
	@Query("SELECT p FROM Product p JOIN FETCH p.store s WHERE p.deletedAt IS NULL AND p.isHidden = false")
	List<Product> findAllActiveProductsWithStore();
}