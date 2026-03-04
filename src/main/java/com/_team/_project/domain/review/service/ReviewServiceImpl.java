package com._team._project.domain.review.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._team._project.domain.review.api.request.ReviewCreateRequest;
import com._team._project.domain.review.api.response.ReviewResponse;
import com._team._project.domain.review.api.response.ReviewUpdateRequest;
import com._team._project.domain.review.entity.Review;
import com._team._project.domain.review.entity.ReviewImage;
import com._team._project.domain.review.repository.ReviewImageRepository;
import com._team._project.domain.review.repository.ReviewRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional

public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewImageRepository reviewImageRepository;

	// CREATE (하나로 합친 버전)
	@Override
	public ReviewResponse createReview(UUID orderId,ReviewCreateRequest request) {
		// 1. 리뷰 엔티티 생성 (Request DTO의 데이터를 사용)
		Review review = Review.builder()
			.orderId(orderId)
			.userId(request.getUserId())
			.storeId(request.getStoreId())
			.rating(request.getRating())
			.content(request.getContent())
			.isHidden(false)
			.build();

		// 2. 리뷰 본체 먼저 저장
		Review savedReview = reviewRepository.save(review);

		// 3. 이미지 URL 리스트가 있다면 반복문으로 저장 (핵심 추가 로직)
		if (request.getImageUrls() != null) {
			for (String url : request.getImageUrls()) {
				ReviewImage image = ReviewImage.builder()
					.review(savedReview) // 위에서 저장된 리뷰와 연결
					.imageUrl(url)
					.build();
				reviewImageRepository.save(image);
			}
		}

		// 4. 응답으로 변환하여 반환
		return toResponse(savedReview);
	}

	// READ
	@Override
	@Transactional(readOnly = true)
	public ReviewResponse getReview(UUID reviewId) {
		Review review = reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
			.orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않거나 삭제되었습니다."));
		return toResponse(review);
	}

	// UPDATE
	@Override
	public ReviewResponse updateReview(UUID reviewId, ReviewUpdateRequest request) {
		Review review = reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
			.orElseThrow(() -> new IllegalArgumentException("수정할 수 없는 리뷰입니다."));

		review.update(request.getRating(), request.getContent());
		return toResponse(review);
	}

	// DELETE
	@Override
	@Transactional
	public void deleteReview(UUID reviewId, UUID userId) {
		// 1. 삭제할 리뷰 찾기 (이미 삭제된 건 제외하고 찾기)
		Review review = reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
			.orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));

		// 2. Soft Delete 수행 (deleted_at, deleted_by 업데이트)
		review.delete(userId);

		// 3. 연관된 이미지들도 Soft Delete 처리 (필요한 경우)
		List<ReviewImage> images = reviewImageRepository.findAllByReviewId(reviewId);
		for (ReviewImage image : images) {
			image.delete(userId);
		}
	}

	// 가게별 리뷰  조회로직
	@Override
	@Transactional(readOnly = true)
	public List<ReviewResponse> getReviewsByStore(UUID storeId) {

		List<Review> reviews = reviewRepository.findAllByStoreIdAndDeletedAtIsNull(storeId);

		// Entity 리스트를 Response DTO 리스트로 변환해서 반환
		return reviews.stream()
			.map(ReviewResponse::from) // Response 변환 로직에 맞게 수정
			.collect(Collectors.toList());
	}


	// Response 변환용 공통 메서드
	private ReviewResponse toResponse(Review review) {
		return new ReviewResponse(
			review.getId(),
			review.getOrderId(),
			review.getUserId(),
			review.getStoreId(),
			review.getRating(),
			review.getContent(),
			review.getIsHidden(),
			review.getCreatedAt(),
			review.getUpdatedAt()
		);
	}



}