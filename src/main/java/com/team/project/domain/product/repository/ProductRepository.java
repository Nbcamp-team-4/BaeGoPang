package com.team.project.domain.product.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.project.domain.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

	// ===== 목록 조회 =====

	// 관리자/점주용: 숨김, 품절 포함 (삭제만 제외)
	List<Product> findAllByStoreIdAndDeletedAtIsNull(UUID storeId);

	// 사용자용: 숨김 제외, 품절 포함
	List<Product> findAllByStoreIdAndDeletedAtIsNullAndIsHiddenFalse(UUID storeId);

	// 관리자/점주용 페이징: 숨김, 품절 포함 (삭제만 제외)
	Page<Product> findAllByStoreIdAndDeletedAtIsNull(UUID storeId, Pageable pageable);

	// 관리자/점주용 페이징 + 상품명 검색
	Page<Product> findAllByStoreIdAndDeletedAtIsNullAndNameContainingIgnoreCase(
		UUID storeId,
		String keyword,
		Pageable pageable
	);


	// ===== 단건 조회 =====

	// 관리자/점주용: 숨김, 품절 포함 (삭제만 제외)
	Optional<Product> findByIdAndDeletedAtIsNull(UUID productId);

	// 사용자용: 숨김 제외, 품절 포함
	Optional<Product> findByIdAndDeletedAtIsNullAndIsHiddenFalse(UUID productId);


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

	@Query("""
		select p
		from Product p
		join fetch p.store s
		where p.deletedAt is null
		  and p.isHidden = false
		  and p.isSoldOut = false
	""")
	List<Product> findAllRecommendableProducts();
}