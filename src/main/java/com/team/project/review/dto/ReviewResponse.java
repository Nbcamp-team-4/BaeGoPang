package com.team.project.review.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
}