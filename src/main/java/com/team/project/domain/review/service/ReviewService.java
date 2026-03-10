package com.team.project.domain.review.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page; // [추가] Page 임포트
import org.springframework.web.multipart.MultipartFile;

import com.team.project.domain.review.api.request.CreateReviewRequest;
import com.team.project.domain.review.api.request.PageReviewRequest; // [확인] 추가된 DTO
import com.team.project.domain.review.api.response.ReviewResponse;
import com.team.project.domain.review.api.response.UpdateReviewRequest;

public interface ReviewService {

	/**
	 * 리뷰 생성 (이미지 포함)
	 */
	ReviewResponse createReview(UUID orderId, UUID userId, CreateReviewRequest request, List<MultipartFile> images);

	/**
	 * 리뷰 단건 조회 (단건 조회는 페이징이 필요 없으므로 request 제거)
	 */
	ReviewResponse getReview(UUID reviewId);

	/**
	 * 리뷰 수정
	 */
	ReviewResponse updateReview(UUID reviewId, UpdateReviewRequest request);

	/**
	 * 리뷰 삭제 (soft delete)
	 */
	void deleteReview(UUID reviewId, UUID userId);

	/**
	 * 가게별 리뷰 목록 조회 (페이징 및 정렬 적용)
	 * 반환 타입을 List -> Page로 변경하고, PageReviewRequest를 인자로 받습니다.
	 */
	Page<ReviewResponse> getReviewsByStore(UUID storeId, PageReviewRequest request);

}