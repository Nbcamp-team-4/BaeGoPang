package com.team.project.domain.review.api.request;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.review.entity.Review;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateReviewRequest {

	private UUID orderId;
	private UUID  userId;
	private UUID storeId;
	private List<String> imageUrls;

	public Review toEntity() {
		return Review.builder()
			.content(this.content)
			.rating(this.rating)
			.orderId(this.orderId)
			.build();
	}

	private Integer rating;
	private String content;
}