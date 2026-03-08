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

	//관리자/점주 숨김 포함 조회
	List<Product> findAllByStoreIdAndDeletedAtIsNull(UUID storeId);

	//사용자 조회
	List<Product> findAllByStoreIdAndDeletedAtIsNullAndIsHiddenFalse(UUID storeId);

	Optional<Product> findByIdAndDeletedAtIsNull(UUID productId);

	@Modifying(clearAutomatically = true) // UPDATE 쿼리 필수 어노테이션
	@Query("UPDATE Product p SET p.deletedAt = CURRENT_TIMESTAMP, p.deletedBy = :userId WHERE p.store.id = :storeId AND p.deletedAt IS NULL")
	void softDeleteByStoreId(@Param("storeId") UUID storeId, @Param("userId") UUID userId);

}
