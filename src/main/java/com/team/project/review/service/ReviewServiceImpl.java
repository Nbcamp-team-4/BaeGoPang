package com.team.project.review.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.team.project.review.dto.ReviewCreateRequest;
import com.team.project.review.dto.ReviewResponse;
import com.team.project.review.dto.ReviewUpdateRequest;
import com.team.project.review.entity.Review;
import com.team.project.review.repository.ReviewRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional

public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;

	// CREATE
	@Override
	public ReviewResponse createReview(ReviewCreateRequest request) {
		Review review = Review.builder() // 생성자보다 Builder 패턴 추천 (필드가 많을 시 유리)
			.orderId(request.getOrderId())
			.userId(request.getUserId())
			.storeId(request.getStoreId())
			.rating(request.getRating())
			.content(request.getContent())
			.isHidden(false) // 기본값 설정
			.build();

		Review savedReview = reviewRepository.save(review);
		return toResponse(savedReview);
	}

	// READ
	@Override
	@Transactional(readOnly = true)
	public ReviewResponse getReview(UUID reviewId) {
		// 규칙: 삭제되지 않은 리뷰만 조회해야 함
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
		// Dirty Checking에 의해 별도의 save 없이도 업데이트됨
		return toResponse(review);
	}

	// DELETE (Soft Delete 적용)
	@Override
	public void deleteReview(UUID reviewId) {
		Review review = reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
			.orElseThrow(() -> new IllegalArgumentException("이미 삭제되었거나 존재하지 않는 리뷰입니다."));

		// 실제 삭제가 아닌 삭제 시각과 삭제자 기록
		// deletedBy는 AuditorAware에서 가져오거나 SecurityContext에서 직접 주입 가능
		review.softDelete();
	}

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