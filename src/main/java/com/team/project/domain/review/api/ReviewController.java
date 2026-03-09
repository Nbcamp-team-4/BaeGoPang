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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.payment.api.response.GetPaymentsResponse;
import com.team.project.domain.review.api.request.CreateReviewRequest;
import com.team.project.domain.review.api.response.ReviewResponse;
import com.team.project.domain.review.api.response.UpdateReviewRequest;
import com.team.project.domain.review.service.ReviewService;
import com.team.project.global.common.dto.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Parameter;
@Tag(name = "Review", description = "리뷰 관리 API")
@RestController
@RequestMapping(value = "/api/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	/**
	 * 리뷰 생성
	 * CreatedBy에 의해 생성자 UUID가 자동으로 기록됩니다.
	 * p_order 와 연결
	 */
	@Operation(summary = "리뷰 작성", description = "주문(orderId)에 대한 리뷰를 작성합니다. 한 주문당 하나의 리뷰만 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "리뷰 작성 성공"),
		@ApiResponse(responseCode = "404", description = "주문 정보를 찾을 수 없음", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = BaseResponse.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
                {
                  "success": false,
                  "data": null,
                  "errorCode": "ORDER_NOT_FOUND"
                }
                """
			)
		))
	})
	@PostMapping("/orders/{orderId}")
	public ResponseEntity<BaseResponse<ReviewResponse>> createReview(
		@Parameter(description = "주문 ID", required = true) @PathVariable UUID orderId,
		@Parameter(description = "사용자 ID 헤더", required = true) @RequestHeader(name = "X-User-Id") UUID userId,
		@RequestBody CreateReviewRequest request) {

		ReviewResponse response = reviewService.createReview(orderId, userId, request);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(BaseResponse.ofSuccess(response));
	}

	/**
	 * 리뷰 상세 조회
	 * 특정 가게의 리뷰  조회 기능
	 */
	@Operation(summary = "리뷰 목록 조회", description = "리뷰를 페이지 단위로 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "결제 목록 조회 성공",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = GetPaymentsResponse.class)
			)
		)
	})
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

	@Operation(summary = "리뷰 수정", description = "작성된 리뷰의 별점이나 내용을 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "리뷰 수정 성공"),
		@ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음")
	})


	@PutMapping("/{reviewId}")
	public ResponseEntity<BaseResponse<ReviewResponse>> updateReview(
		@Parameter(description = "리뷰 ID", required = true) @PathVariable UUID reviewId,
		@RequestBody UpdateReviewRequest request) {

		ReviewResponse response = reviewService.updateReview(reviewId, request);

		return ResponseEntity.ok(BaseResponse.ofSuccess(response));
	}

	@Operation(summary = "리뷰 삭제", description = "리뷰를 삭제 처리(Soft Delete)합니다.")
		@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "리뷰 삭제 성공"),
		@ApiResponse(responseCode = "403", description = "삭제 권한 없음")
		})

	@DeleteMapping("/{reviewId}")
	public ResponseEntity<BaseResponse<Void>> deleteReview(
		@Parameter(description = "리뷰 ID", required = true) @PathVariable UUID reviewId,
		@Parameter(description = "사용자 ID 헤더", required = true) @RequestHeader(name = "X-User-Id") UUID userId) {

		reviewService.deleteReview(reviewId, userId);

		return ResponseEntity.ok(BaseResponse.ofSuccess(null));
	}
}
