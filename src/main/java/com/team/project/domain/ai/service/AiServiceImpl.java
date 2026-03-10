package com.team.project.domain.ai.service;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	private final ReviewRepository reviewRepository;
	private final ProductRepository productRepository;
	private final ChatClient chatClient;

	@Override
	@Transactional(readOnly = true)
	public List<SearchAiRecommendResponse> recommendMenu(ProcessAiRecommendRequest request) {

		List<Product> products = productRepository.findAllRecommendableProducts();

		return products.stream()
				.map(product -> {
					int matchRate = calculateMatchRate(product, request);

					return SearchAiRecommendResponse.builder()
							.name(product.getName())
							.storeName(product.getStore().getName())
							.price(product.getPrice())
							.description(buildDescription(product, request, matchRate))
							.matchRate(matchRate)
							.build();
				})
				.filter(response -> response.matchRate() > 0)
				.sorted((a, b) -> Integer.compare(b.matchRate(), a.matchRate()))
				.limit(3)
				.toList();
	}

	private int calculateMatchRate(Product product, ProcessAiRecommendRequest request) {
		int score = 0;

		String name = product.getName() != null ? product.getName() : "";
		String description = product.getDescription() != null ? product.getDescription() : "";
		String text = (name + " " + description).toLowerCase();

		String mood = request.mood();
		String people = request.people();
		String budget = request.budget();
		Integer price = product.getPrice();

		// 1. 기분/메뉴 취향 점수
		if ("🌶️ 매운 게 땡겨요".equals(mood)) {
			if (containsAny(text, "매운", "마라", "짬뽕", "떡볶이", "불닭", "제육", "쭈꾸미")) {
				score += 50;
			}
		} else if ("🥣 따뜻한 국물".equals(mood)) {
			if (containsAny(text, "국", "국밥", "탕", "찌개", "전골", "칼국수", "라면", "우동", "쌀국수")) {
				score += 50;
			}
		} else if ("🥗 가볍게".equals(mood)) {
			if (containsAny(text, "샐러드", "포케", "샌드위치", "랩", "요거트", "과일", "서브")) {
				score += 50;
			}
		} else if ("🍖 고기가 땡겨요".equals(mood)) {
			if (containsAny(text, "고기", "삼겹", "갈비", "불고기", "치킨", "보쌈", "족발", "스테이크", "돈까스")) {
				score += 50;
			}
		} else if ("🍜 면 요리".equals(mood)) {
			if (containsAny(text, "면", "국수", "파스타", "라면", "우동", "소바", "짜장", "짬뽕", "쌀국수")) {
				score += 50;
			}
		} else if ("🎲 아무거나".equals(mood)) {
			score += 25;
		}

		// 2. 예산 점수
		if (price != null) {
			if ("1만원 이하".equals(budget) && price <= 10000) {
				score += 30;
			} else if ("1~2만원".equals(budget) && price > 10000 && price <= 20000) {
				score += 30;
			} else if ("2만원 이상".equals(budget) && price > 20000) {
				score += 30;
			}
		}

		// 3. 인원 점수
		if ("혼자".equals(people)) {
			if (containsAny(text, "1인", "혼밥", "덮밥", "국밥", "라면", "파스타", "샐러드", "돈까스")) {
				score += 20;
			} else {
				score += 10;
			}
		} else if ("2~3명".equals(people)) {
			if (containsAny(text, "세트", "중", "피자", "치킨", "족발", "보쌈", "파스타")) {
				score += 20;
			} else {
				score += 10;
			}
		} else if ("4명 이상".equals(people)) {
			if (containsAny(text, "대", "전골", "세트", "피자", "치킨", "족발", "보쌈", "패밀리")) {
				score += 20;
			} else {
				score += 10;
			}
		}

		// 4. 기본 점수
		if (score == 0) {
			score = 10;
		}

		return Math.min(score, 100);
	}

	private String buildDescription(Product product, ProcessAiRecommendRequest request, int matchRate) {
		StringBuilder reason = new StringBuilder();

		reason.append(request.mood()).append(" 기분에 맞춰 추천했어요. ");

		if (product.getPrice() != null) {
			reason.append("가격은 ").append(String.format("%,d", product.getPrice())).append("원으로 ");
			if ("1만원 이하".equals(request.budget()) && product.getPrice() <= 10000) {
				reason.append("예산에 부담이 적어요. ");
			} else if ("1~2만원".equals(request.budget()) && product.getPrice() > 10000 && product.getPrice() <= 20000) {
				reason.append("적당한 예산대에 잘 맞아요. ");
			} else if ("2만원 이상".equals(request.budget()) && product.getPrice() > 20000) {
				reason.append("여유 있게 즐기기 좋아요. ");
			}
		}

		if (product.getDescription() != null && !product.getDescription().isBlank()) {
			reason.append(product.getDescription()).append(" ");
		}

		reason.append("추천 일치도는 ").append(matchRate).append("%예요.");

		return reason.toString().trim();
	}

	private boolean containsAny(String text, String... keywords) {
		for (String keyword : keywords) {
			if (text.contains(keyword.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public GetReviewReplyResponse generateReviewReply(UUID reviewId, String tone) {

		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new IllegalArgumentException("ID가 " + reviewId + "인 리뷰를 찾을 수 없습니다."));

		String storeName = (review.getStore() != null) ? review.getStore().getName() : "우리 가게";

		String prompt = String.format(
				"너는 맛집 사장님이야. 아래 고객 리뷰에 대해 [%s] 말투로 친절하고 정중한 답글을 작성해줘.\n\n" +
						"가게 이름: %s\n" +
						"고객 리뷰: %s\n\n" +
						"답글 초안:",
				tone, storeName, review.getContent()
		);

		String reply = chatClient.prompt(prompt)
				.call()
				.content();

		return GetReviewReplyResponse.builder()
				.reviewId(review.getId())
				.aiGeneratedReply(reply)
				.build();
	}
}