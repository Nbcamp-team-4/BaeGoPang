package com.team.project.domain.ai.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.ai.api.request.ProcessAiRecommendRequest;
import com.team.project.domain.ai.api.request.ProcessReviewReplyAiRequest;
import com.team.project.domain.ai.api.response.GetReviewReplyResponse;
import com.team.project.domain.ai.api.response.SearchAiRecommendResponse;
import com.team.project.domain.ai.service.AiService;
import com.team.project.global.common.dto.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "AI", description = "AI 메뉴 추천 및 리뷰 답글 생성 API")
@RestController
@RequestMapping(value = "/api/ai", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
public class AiController {

	private final AiService aiService;

	@Operation(summary = "AI 메뉴 추천", description = "사용자의 취향이나 상황에 맞는 메뉴를 AI가 추천해줍니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "메뉴 추천 성공")
	})
	@PostMapping("/recommend")
	public ResponseEntity<BaseResponse<List<SearchAiRecommendResponse>>> getRecommend(
		@RequestBody ProcessAiRecommendRequest request) {

		List<SearchAiRecommendResponse> response = aiService.recommendMenu(request);
		return ResponseEntity.ok(BaseResponse.ofSuccess(response));
	}

	@Operation(summary = "리뷰 AI 답글 생성", description = "리뷰 내용과 원하는 말투(tone)를 바탕으로 AI가 자동으로 답글을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "답글 생성 성공"),
		@ApiResponse(responseCode = "404", description = "해당 리뷰를 찾을 수 없음", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = BaseResponse.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
                {
                  "success": false,
                  "data": null,
                  "errorCode": "REVIEW_NOT_FOUND"
                }
                """
			)
		))
	})
	@PostMapping("/review-reply")
	public ResponseEntity<BaseResponse<GetReviewReplyResponse>> generateReviewReply(
		@RequestBody ProcessReviewReplyAiRequest request) {

		GetReviewReplyResponse response = aiService.generateReviewReply(request.reviewId(), request.tone());
		return ResponseEntity.ok(BaseResponse.ofSuccess(response));
	}
}