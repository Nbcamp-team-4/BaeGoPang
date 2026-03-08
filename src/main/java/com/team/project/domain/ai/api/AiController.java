package com.team.project.domain.ai.api;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.ai.api.request.ProcessAiRecommendRequest;
import com.team.project.domain.ai.api.request.ProcessReviewReplyAiRequest;
import com.team.project.domain.ai.api.response.GetReviewReplyResponse;
import com.team.project.domain.ai.api.response.SearchAiRecommendResponse;
import com.team.project.domain.ai.service.AiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

	private final AiService aiService;

	// 기존 메뉴 추천 API
	@PostMapping("/recommend")
	public List<SearchAiRecommendResponse> getRecommend(@RequestBody ProcessAiRecommendRequest request) {
		return aiService.recommendMenu(request);
	}

	/**
	 * 리뷰 AI 답글 생성 API 추가!
	 */
	@PostMapping("/review-reply")
	public GetReviewReplyResponse generateReviewReply(@RequestBody ProcessReviewReplyAiRequest request) {
		// aiService.generateReviewReply가 이제 객체를 반환하므로 바로 return 가능합니다.
		return aiService.generateReviewReply(request.reviewId(), request.tone());
	}
}