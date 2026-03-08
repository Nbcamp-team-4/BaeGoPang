package com.team.project.domain.review.api.request;

import java.util.List;
import java.util.UUID; // <--- 필수 임포트
import com.team.project.domain.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateReviewRequest {

	private UUID orderId;
	private UUID userId;
	private UUID storeId;
	private List<String> imageUrls;
	private Integer rating;
	private String content;

	// Service에서 넘겨받은 store 객체를 사용하여 엔티티를 만듭니다.
	public Review toEntity(UUID orderId) {
		return Review.builder()
			.orderId(orderId)
			.userId(this.userId)
			.rating(this.rating)
			.content(this.content)
			.isHidden(false) // 기본값 설정
			.build();
	}
}