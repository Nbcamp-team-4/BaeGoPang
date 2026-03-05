package com._team._project.domain.review.service; // 1. 패키지 경로 추가

import java.util.List;
import java.util.UUID;

import com._team._project.domain.review.api.request.CreateReviewRequest;
import com._team._project.domain.review.api.response.ReviewResponse;
import com._team._project.domain.review.api.response.UpdateReviewRequest;

public interface ReviewService {

	//  매개변수 이름을 reviewId로 통일
	ReviewResponse createReview(UUID orderId, CreateReviewRequest request);

	ReviewResponse getReview(UUID reviewId);

	ReviewResponse updateReview(UUID reviewId, UpdateReviewRequest request);

	void deleteReview(UUID reviewId, UUID userId); //누가 지웠는지 추가

	//  (가게별 조회)
	List<ReviewResponse> getReviewsByStore(UUID storeId);

}