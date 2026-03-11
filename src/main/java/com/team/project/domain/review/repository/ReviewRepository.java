package com.team.project.domain.review.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.project.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

	// 1번 기능: 특정 주문에 대해 이미 리뷰가 있는지 확인 (에러 해결용!)
	boolean existsByOrderId(UUID orderId);

	// 공통: 삭제되지 않은 리뷰 단건 조회
	Optional<Review> findByIdAndDeletedAtIsNull(UUID reviewId);

	@Query(value = "select distinct r from Review r " +
		"left join fetch r.reviewImages " +
		"where r.store.id = :storeId and r.deletedAt is null",
		countQuery = "select count(r) from Review r " +
			"where r.store.id = :storeId and r.deletedAt is null")
	Page<Review> findAllByStoreIdWithImages(@Param("storeId") UUID storeId, Pageable pageable);
}