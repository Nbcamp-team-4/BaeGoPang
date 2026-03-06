package com.team.project.domain.review.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.review.entity.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, UUID> {
	// 리뷰 ID로 모든 리뷰 이미지 리스트 찾기
	List<ReviewImage> findAllByReviewId(UUID reviewId);
}