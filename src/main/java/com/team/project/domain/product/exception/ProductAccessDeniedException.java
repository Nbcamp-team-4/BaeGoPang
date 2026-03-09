package com.team.project.domain.product.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class ProductAccessDeniedException extends BaseException {

	public ProductAccessDeniedException() {
		super(ProductErrorCode.PRODUCT_ACCESS_DENIED.name(), HttpStatus.FORBIDDEN);
	}
}