package com.team.project.domain.product.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductErrorCode {

	// 400
	INVALID_PRODUCT_REQUEST("요청 값이 올바르지 않습니다."),
	INVALID_PRODUCT_PRICE("상품 가격은 0 이상이어야 합니다."),
	INVALID_PRODUCT_NAME("상품명은 비어 있을 수 없습니다."),
	INVALID_PRODUCT_DESCRIPTION("상품 설명이 올바르지 않습니다."),

	// 403
	PRODUCT_ACCESS_DENIED("해당 상품에 대한 권한이 없습니다."),

	// 404
	PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다."),
	PRODUCT_OPTION_NOT_FOUND("상품 옵션 그룹을 찾을 수 없습니다."),
	PRODUCT_OPTION_ITEM_NOT_FOUND("상품 옵션 항목을 찾을 수 없습니다.");

	private final String message;
}