package com.team.project.domain.product.exception;

public class ProductOptionItemNotFoundException extends RuntimeException {

	public ProductOptionItemNotFoundException() {
		super("상품 옵션 아이템을 찾을 수 없습니다.");
	}
}