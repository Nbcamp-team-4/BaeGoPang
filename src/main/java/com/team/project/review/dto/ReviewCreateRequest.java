package com.team.project.review.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class ReviewCreateRequest {

	private UUID orderId;
	private UUID  userId;
	private UUID storeId;

	private Integer rating;
	private String content;
}