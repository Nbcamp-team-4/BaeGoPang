package com.team.project.domain.product.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class ProductNotFoundException extends BaseException {

	public ProductNotFoundException() {
		super(ProductErrorCode.PRODUCT_NOT_FOUND.name(), HttpStatus.NOT_FOUND);
	}
}