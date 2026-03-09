package com.team.project.domain.review.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.review.api.request.CreateReviewRequest;
import com.team.project.domain.review.api.response.ReviewResponse;
import com.team.project.domain.review.api.response.UpdateReviewRequest;
import com.team.project.domain.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews") // 규칙: /api/ prefix 추가 및 복수형 유지
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	/**
	 * 리뷰 생성
	 * @CreatedBy에 의해 생성자 UUID가 자동으로 기록됩니다.
	 * p_order 와 연결
	 */
	@PostMapping("/orders/{orderId}")
	public ResponseEntity<ReviewResponse> createReview(
		@PathVariable UUID orderId,
		@RequestHeader(name = "X-User-Id") UUID userId, // 인증된 유저 ID 헤더
		@RequestBody CreateReviewRequest request) {

		// 서비스에서 주문 완료 상태 및 중복 여부를 체크하도록 설계
		ReviewResponse response = reviewService.createReview(orderId, userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	/**
	 * 리뷰 상세 조회
	 * 특정 가게의 리뷰  조회 기능
	 */
	@GetMapping("/stores/{storeId}")
	public ResponseEntity<List<ReviewResponse>> getStoreReviews(@PathVariable UUID storeId) {
		// 가게별 리뷰 조회 기능 추가 시
		return ResponseEntity.ok(reviewService.getReviewsByStore(storeId));
	}


	/**
	 * 2번 기능: 리뷰 리스트 조회 (Query String 방식)
	 * 요구사항: 가게 PK값을 쿼리스트링에 담아 호출 (예: /api/reviews?storeId=...)
	 */
	@GetMapping
	public ResponseEntity<List<ReviewResponse>> getReviewsByStore(
		@RequestParam(name = "storeId") UUID storeId) {

		List<ReviewResponse> responses = reviewService.getReviewsByStore(storeId);
		return ResponseEntity.ok(responses);
	}


	/**
	 * 리뷰 수정
	 * @LastModifiedBy에 의해 수정자 UUID가 자동으로 기록됩니다.
	 */
	@PutMapping("/{reviewId}")
	public ResponseEntity<ReviewResponse> updateReview(
		@PathVariable UUID reviewId,
		@RequestBody UpdateReviewRequest request) {
		ReviewResponse response = reviewService.updateReview(reviewId, request);
		return ResponseEntity.ok(response);
	}

	/**
	 * 리뷰 삭제 (Soft Delete)
	 * 테이블 설계의 deleted_at, deleted_by를 업데이트하도록 서비스에서 처리됩니다.
	 */
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Void> deleteReview(
		@PathVariable UUID reviewId,
		@RequestHeader(name = "-User-Id") UUID userId // 헤더에서 유저 ID를 받아옵니다
	) {
		reviewService.deleteReview(reviewId, userId); // 이제 인자 2개를 모두 전달!
		return ResponseEntity.noContent().build();
	}
}