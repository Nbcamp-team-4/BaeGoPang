package com.team.project.domain.ai.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.team.project.domain.ai.api.request.ProcessAiRecommendRequest;
import com.team.project.domain.ai.api.response.SearchAiRecommendResponse;
import com.team.project.domain.ai.repository.AiLogRepository;
import com.team.project.domain.product.entity.Product;
import com.team.project.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService { // 1. 인터페이스 구현 명시

	private final ProductRepository productRepository;
	private final AiLogRepository aiLogRepository;
	private final ChatClient chatClient;

	@Override // 2. @Override 어노테이션 추가
	@Transactional(readOnly = true)
	public List<SearchAiRecommendResponse> recommendMenu(ProcessAiRecommendRequest request) { // 3. 타입 일치 확인

		List<Product> products = productRepository.findAll();

		String menuContext = products.stream()
			.map(p -> String.format("가게:%s, 메뉴:%s, 가격:%d, 설명:%s",
				p.getStore().getName(), p.getName(), p.getPrice(), p.getDescription()))
			.collect(Collectors.joining(" | "));

		String userPrompt = String.format(
			"사용자 상황 - 기분: %s, 예산: %s. 메뉴 목록: [%s]. " +
				"JSON 배열 형태로 응답해줘. 필드명: name, storeName, price, description, matchRate",
			request.mood(), request.budget(), menuContext
		);

		String aiRawResponse = chatClient.prompt(userPrompt).call().content();

		// 4. 일단 기존에 작성하신 변환 메서드를 호출하여 반환
		return convertToResponseList(products);
	}

	private List<SearchAiRecommendResponse> convertToResponseList(List<Product> products) {
		return products.stream()
			.limit(3)
			.map(p -> SearchAiRecommendResponse.builder()
				.name(p.getName())
				.storeName(p.getStore().getName())
				.price(p.getPrice())
				.description(p.getDescription())
				.matchRate(90)
				.build())
			.toList();
	}
}