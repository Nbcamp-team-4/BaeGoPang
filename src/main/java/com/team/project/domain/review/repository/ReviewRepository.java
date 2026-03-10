package com.team.project.domain.review.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.team.project.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

	// 1번 기능: 특정 주문에 대해 이미 리뷰가 있는지 확인 (에러 해결용!)
	boolean existsByOrderId(UUID orderId);

	// 공통: 삭제되지 않은 리뷰 단건 조회
	Optional<Review> findByIdAndDeletedAtIsNull(UUID reviewId);

	// 2번 기능: 특정 가게의 삭제되지 않은 모든 리뷰 조회
	Page <Review> findAllByStoreIdAndDeletedAtIsNull(UUID storeId, Pageable pageable);


}