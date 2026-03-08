package com.team.project.domain.review.api.request;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.review.entity.Review;
import com.team.project.domain.store.entity.Store;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateReviewRequest {

	@NotNull(message = "주문 ID는 필수입니다.")
	private UUID orderId;

	// userId와 storeId는 보통 Service에서 주문 정보를 통해 가져오는 것이 더 안전하지만,
	// DTO에 포함되어 있다면 그대로 활용하되, 평점 제한을 추가합니다.

	@NotNull(message = "평점은 필수입니다.")
	@Min(value = 1, message = "평점은 최소 1점 이상이어야 합니다.")
	@Max(value = 5, message = "평점은 최대 5점 이하이어야 합니다.")
	private Integer rating;

	@NotBlank(message = "리뷰 내용을 입력해주세요.")
	private String content;

	private List<String> imageUrls; // 리뷰 이미지 기능용

	// 수정된 toEntity: storeId를 포함하여 나중에 가게별 평균 평점 계산을 쉽게 합니다.
	public Review toEntity(UUID userId, UUID orderId, Store store) { // Store 객체를 파라미터로 받음
		return Review.builder()
			.userId(userId)
			.orderId(orderId)
			.store(store) // .storeId(storeId) 대신 .store(store) 사용!
			.rating(this.rating)
			.content(this.content)
			.isHidden(false)
			.build();
	}
}