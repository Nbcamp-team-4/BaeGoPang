package com.team.project.domain.review.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team.project.domain.review.api.request.CreateReviewRequest;
import com.team.project.domain.review.api.response.ReviewResponse;
import com.team.project.domain.review.api.response.UpdateReviewRequest;
import com.team.project.domain.review.service.ReviewService;
import com.team.project.global.common.dto.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Tag(name = "Review", description = "리뷰 관리 API")
@RestController
@RequestMapping(value = "/api/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	/**
	 * 리뷰 생성 (이미지 포함)
	 */
	@Operation(summary = "리뷰 작성", description = "주문(orderId)에 대한 리뷰와 사진을 작성합니다.")
	@PostMapping(value = "/orders/{orderId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // [수정] Multipart 설정
	public ResponseEntity<BaseResponse<ReviewResponse>> createReview(
		@Parameter(description = "주문 ID") @PathVariable UUID orderId,
		@Parameter(description = "사용자 ID 헤더") @RequestHeader(name = "X-User-Id") UUID userId,
		@RequestPart("request") CreateReviewRequest request, // [수정] @RequestBody -> @RequestPart
		@RequestPart(value = "images", required = false) List<MultipartFile> images) { // [추가] 이미지 파라미터

		// [주의] ReviewService 인터페이스도 images를 받도록 수정해야 합니다.
		ReviewResponse response = reviewService.createReview(orderId, userId, request, images);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(BaseResponse.ofSuccess(response));
	}

	@Operation(summary = "리뷰 목록 조회", description = "특정 가게의 리뷰를 조회합니다.")
	@GetMapping("/stores/{storeId}")
	public ResponseEntity<List<ReviewResponse>> getStoreReviews(@PathVariable UUID storeId) {
		return ResponseEntity.ok(reviewService.getReviewsByStore(storeId));
	}

	@Operation(summary = "리뷰 수정")
	@PutMapping("/{reviewId}")
	public ResponseEntity<BaseResponse<ReviewResponse>> updateReview(
		@PathVariable UUID reviewId,
		@RequestBody UpdateReviewRequest request) {
		ReviewResponse response = reviewService.updateReview(reviewId, request);
		return ResponseEntity.ok(BaseResponse.ofSuccess(response));
	}

	@Operation(summary = "리뷰 삭제")
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<BaseResponse<Void>> deleteReview(
		@PathVariable UUID reviewId,
		@RequestHeader(name = "X-User-Id") UUID userId // [수정] "-User-Id" -> "X-User-Id" 오타 수정
	) {
		reviewService.deleteReview(reviewId, userId);
		return ResponseEntity.ok(BaseResponse.ofSuccess(null));
	}
}