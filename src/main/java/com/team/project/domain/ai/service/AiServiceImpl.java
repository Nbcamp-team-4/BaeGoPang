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
import com.team.project.domain.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

	private final ProductRepository productRepository;
	private final ReviewRepository reviewRepository;
	private final ChatClient chatClient; // Spring AI ChatClient 사용
	private final ObjectMapper objectMapper; // 생성자 주입 권장

	@Override
	@Transactional(readOnly = true)
	public List<SearchAiRecommendResponse> recommendMenu(ProcessAiRecommendRequest request) {
		// 1. [수정] Fetch Join을 사용한 레포지토리 메서드로 호출하세요 (성능 최적화)
		List<Product> products = productRepository.findAllActiveProductsWithStore();

		if (products.isEmpty()) {
			return List.of(); // 메뉴가 없으면 빈 리스트 반환
		}

		// 2. AI에게 전달할 메뉴 텍스트 생성
		Collections.shuffle(products);
		String menuContext = products.stream()
			.limit(10)
			.map(p -> String.format("- %s (가게: %s, 가격: %d원, 설명: %s)",
				p.getName(), p.getStore().getName(), p.getPrice(), p.getDescription()))
			.collect(Collectors.joining("\n"));

		// 3. [보완] AI 프롬프트 - 백틱 금지 명령 추가
		String systemPrompt = "너는 맛집 추천 전문가야. 반드시 JSON 배열로만 응답해. 마크다운 백틱(```)을 절대 사용하지 마.";
		String userPrompt = String.format(
			"[상황] 기분: %s, 인원: %s, 예산: %s\n\n" +
				"[우리 동네 메뉴 리스트]\n%s\n\n" +
				"위 리스트에서만 3개를 골라 name, storeName, price, description, matchRate(0~100) 필드로 구성된 JSON 배열을 출력해줘.",
			request.mood(), request.people(), request.budget(), menuContext
		);

		// 4. AI 호출
		String responseJson = chatClient.prompt()
			.system(systemPrompt)
			.user(userPrompt)
			.call()
			.content();

		return parseAiResponse(responseJson);
	}

	// 5. [수정] 문자열 청소 로직 추가 (백틱 제거)
	private List<SearchAiRecommendResponse> parseAiResponse(String json) {
		try {
			// AI가 혹시라도 백틱을 붙였을 경우를 대비해 청소합니다.
			String cleanedJson = json.replaceAll("(?s)```json\\s*|```", "").trim();
			return objectMapper.readValue(cleanedJson, new TypeReference<List<SearchAiRecommendResponse>>() {});
		} catch (Exception e) {
			System.err.println("AI 응답 파싱 실패: " + e.getMessage());
			// 실패 시 사용자에게 보여줄 더미 데이터나 빈 리스트를 반환합니다.
			return List.of();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public GetReviewReplyResponse generateReviewReply(UUID reviewId, String tone) {
		// 임시로 null을 반환하거나 기본 로직을 작성합니다.
		return GetReviewReplyResponse.builder()
			.reviewId(reviewId)
			.aiGeneratedReply("답글 생성 중입니다...")
			.build();
	}
}