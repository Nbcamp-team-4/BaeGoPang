package com._team._project.domain.review.api.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com._team._project.domain.review.entity.Review;
import com._team._project.domain.review.entity.ReviewImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	private List<String> imageUrls;

	public static ReviewResponse from(Review review) {
		return ReviewResponse.builder()
			.id(review.getId())
			.orderId(review.getOrderId())
			.userId(review.getUserId())
			.storeId(review.getStoreId())
			.rating(review.getRating())
			.content(review.getContent())
			.isHidden(review.getIsHidden())
			.createdAt(review.getCreatedAt())
			.updatedAt(review.getUpdatedAt())
			// 이미지가 없으면 null이 아니라 빈 리스트를 넣어줍니다.
			.imageUrls(review.getReviewImages() != null ?
				review.getReviewImages().stream()
					.filter(img -> img.getDeletedAt() == null)
					.map(ReviewImage::getImageUrl)
					.collect(Collectors.toList()) : new ArrayList<>())
			.build();
	}
}