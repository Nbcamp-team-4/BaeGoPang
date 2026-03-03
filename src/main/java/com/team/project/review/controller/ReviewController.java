package com.team.project.review.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team.project.review.dto.ReviewCreateRequest;
import com.team.project.review.dto.ReviewResponse;
import com.team.project.review.dto.ReviewUpdateRequest;
import com.team.project.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews") // 규칙: /api/ prefix 추가 및 복수형 유지
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	/**
	 * 리뷰 생성
	 * @CreatedBy에 의해 생성자 UUID가 자동으로 기록됩니다.
	 */
	@PostMapping
	public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewCreateRequest request) {
		ReviewResponse response = reviewService.createReview(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 리뷰 상세 조회
	 * @param reviewId 카멜케이스 규칙 적용
	 */
	@GetMapping("/{reviewId}")
	public ResponseEntity<ReviewResponse> getReview(@PathVariable UUID reviewId) {
		ReviewResponse response = reviewService.getReview(reviewId);
		return ResponseEntity.ok(response);
	}

	/**
	 * 리뷰 수정
	 * @LastModifiedBy에 의해 수정자 UUID가 자동으로 기록됩니다.
	 */
	@PutMapping("/{reviewId}")
	public ResponseEntity<ReviewResponse> updateReview(
		@PathVariable UUID reviewId,
		@RequestBody ReviewUpdateRequest request) {
		ReviewResponse response = reviewService.updateReview(reviewId, request);
		return ResponseEntity.ok(response);
	}

	/**
	 * 리뷰 삭제 (Soft Delete)
	 * 테이블 설계의 deleted_at, deleted_by를 업데이트하도록 서비스에서 처리됩니다.
	 */
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<String> deleteReview(@PathVariable UUID reviewId) {
		reviewService.deleteReview(reviewId);
		return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
	}
}