package com.team.project.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateRequest {
	private int rating;         // 수정할 별점
	private String content;     // 수정할 내용
}