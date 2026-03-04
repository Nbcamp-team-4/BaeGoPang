package com._team._project.domain.review.api.response;

import java.time.LocalDateTime;
import java.util.UUID;
import com._team._project.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

	private UUID id;
	private UUID orderId;
	private UUID userId;
	private UUID storeId;
	private Integer rating;
	private String content;
	private Boolean isHidden;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static ReviewResponse from(Review review) {
		return ReviewResponse.builder()
			.id(review.getId())
			.content(review.getContent())
			// .images(...) // 나중에 이미지 리스트도 여기에 추가!
			.build();
	}



}