package com.team.project.domain.ai.service;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.ai.api.request.ProcessAiRecommendRequest;
import com.team.project.domain.ai.api.response.GetReviewReplyResponse;
import com.team.project.domain.ai.api.response.SearchAiRecommendResponse;
import com.team.project.domain.review.entity.Review;
import com.team.project.domain.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

	private final ReviewRepository reviewRepository;
	private final ChatClient chatClient;

	@Override
	@Transactional(readOnly = true)
	public List<SearchAiRecommendResponse> recommendMenu(ProcessAiRecommendRequest request) {
		return List.of();
	}

	@Override
	@Transactional(readOnly = true)
	public GetReviewReplyResponse generateReviewReply(UUID reviewId, String tone) {

		// 1. 리뷰 조회 (예외 메시지 구체화)
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new IllegalArgumentException("ID가 " + reviewId + "인 리뷰를 찾을 수 없습니다."));

		// 2. 가게 이름 안전하게 가져오기 (Null 방지)
		String storeName = (review.getStore() != null) ? review.getStore().getName() : "우리 가게";

		// 3. AI 프롬프트 구성 (요청 사항을 더 구체화하여 품질 향상)
		String prompt = String.format(
			"너는 맛집 사장님이야. 아래 고객 리뷰에 대해 [%s] 말투로 친절하고 정중한 답글을 작성해줘.\n\n" +
				"가게 이름: %s\n" +
				"고객 리뷰: %s\n\n" +
				"답글 초안:",
			tone, storeName, review.getContent()
		);

		// 4. Spring AI 호출
		String reply = chatClient.prompt(prompt)
			.call()
			.content();

		// 5. 결과 반환 (Builder 패턴 사용)
		return GetReviewReplyResponse.builder()
			.reviewId(review.getId())
			.aiGeneratedReply(reply)
			.build();
	}
}