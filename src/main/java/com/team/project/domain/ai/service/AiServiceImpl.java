package com.team.project.domain.ai.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.project.domain.ai.api.request.ProcessAiRecommendRequest;
import com.team.project.domain.ai.api.response.GetReviewReplyResponse;
import com.team.project.domain.ai.api.response.SearchAiRecommendResponse;
import com.team.project.domain.product.entity.Product;
import com.team.project.domain.product.repository.ProductRepository;
import com.team.project.domain.review.entity.Review;
import com.team.project.domain.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {


	private final ProductRepository productRepository;
	private final ReviewRepository reviewRepository;
	private final ChatClient chatClient;

	@Override
	@Transactional(readOnly = true)
	public List<SearchAiRecommendResponse> recommendMenu(ProcessAiRecommendRequest request) {
		// 1. DB에서 추천 후보가 될 메뉴들을 가져옵니다.
		List<Product> products = productRepository.findAll();

		// 2. AI에게 전달할 메뉴 텍스트 생성 (다양성을 위해 shuffle 추천)
		Collections.shuffle(products);
		String menuContext = products.stream()
			.limit(10) // 너무 많으면 AI가 혼란스러우니 상위 10개만
			.map(p -> String.format("- %s (가게: %s, 가격: %d원, 설명: %s)",
				p.getName(), p.getStore().getName(), p.getPrice(), p.getDescription()))
			.collect(Collectors.joining("\n"));

		// 3. AI 프롬프트 작성
		String prompt = String.format(
			"너는 맛집 추천 전문가야. 아래 상황에 맞는 메뉴 3개를 '우리 동네 메뉴 리스트'에서만 골라줘.\n\n" +
				"[상황] 기분: %s, 인원: %s, 예산: %s\n\n" +
				"[우리 동네 메뉴 리스트]\n%s\n\n" +
				"조건: 반드시 JSON 배열 형식으로만 대답해. 필드는 name, storeName, price, description, matchRate(0~100)로 구성해.",
			request.mood(), request.people(), request.budget(), menuContext
		);

		// 4. AI 호출 및 결과 반환
		String responseJson = chatClient.prompt(prompt).call().content();
		return parseAiResponse(responseJson); // JSON 문자열을 List로 변환하는 메서드 필요
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

	// AiServiceImpl.java 클래스 내부 하단에 추가
	private List<SearchAiRecommendResponse> parseAiResponse(String json) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			// AI가 준 JSON 문자열을 List<SearchAiRecommendResponse> 구조로 변환
			return objectMapper.readValue(json, new TypeReference<List<SearchAiRecommendResponse>>() {});
		} catch (Exception e) {
			// 파싱 실패 시 빈 리스트 반환 (로그 기록 권장)
			System.err.println("AI 응답 파싱 실패: " + e.getMessage());
			return List.of();
		}
	}

}