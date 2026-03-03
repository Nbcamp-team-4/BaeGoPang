package com.team.project.review.repository;

import com.team.project.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
	// 삭제되지 않은(deletedAt이 null인) 리뷰만 찾는 메서드 추가
	Optional<Review> findByIdAndDeletedAtIsNull(UUID reviewId);
}