package com.team.project.domain.product.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class ProductOptionNotFoundException extends BaseException {

	public ProductOptionNotFoundException() {
		super(ProductErrorCode.PRODUCT_OPTION_NOT_FOUND.name(), HttpStatus.NOT_FOUND);
	}
}
