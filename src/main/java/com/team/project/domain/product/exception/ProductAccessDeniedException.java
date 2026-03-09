package com.team.project.domain.product.exception;

public class ProductAccessDeniedException extends RuntimeException {

	public ProductAccessDeniedException() {
		super("해당 상품에 접근할 권한이 없습니다.");
	}
}