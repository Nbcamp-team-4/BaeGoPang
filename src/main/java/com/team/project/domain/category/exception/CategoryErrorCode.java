package com.team.project.domain.category.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryErrorCode {

	// 400
	INVALID_CATEGORY_REQUEST("요청 값이 올바르지 않습니다."),

	// 409
	CATEGORY_DUPLICATE("이미 존재하는 카테고리입니다."),

	// 404
	CATEGORY_NOT_FOUND("카테고리를 찾을 수 없습니다.");

	private final String message;
}