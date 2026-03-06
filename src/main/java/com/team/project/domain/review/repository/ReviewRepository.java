package com.team.project.domain.review.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
	// 삭제되지 않은(deletedAt이 null인) 리뷰만 찾는 메서드 추가
	Optional<Review> findByIdAndDeletedAtIsNull(UUID reviewId);

	//  유저가 쓴 리뷰중 삭제되지 않는 것만 조회
	List<Review> findAllByStoreIdAndDeletedAtIsNull(UUID storeId);
}