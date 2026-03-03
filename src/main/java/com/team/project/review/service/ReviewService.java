package com.team.project.review.service; // 1. 패키지 경로 추가

import java.util.UUID; // 2. UUID 심볼 해결을 위한 임포트

import com.team.project.review.dto.ReviewCreateRequest;
import com.team.project.review.dto.ReviewResponse;
import com.team.project.review.dto.ReviewUpdateRequest;

public interface ReviewService {

	// 3. 매개변수 이름을 reviewId로 통일 (API 규칙 반영)
	ReviewResponse createReview(ReviewCreateRequest request);

	ReviewResponse getReview(UUID reviewId);

	ReviewResponse updateReview(UUID reviewId, ReviewUpdateRequest request);

	void deleteReview(UUID reviewId);
}