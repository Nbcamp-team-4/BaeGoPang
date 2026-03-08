package com.team.project.domain.product.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class ProductNotFoundException extends BaseException {
	public ProductNotFoundException() {
		super("PRODUCT_NOT_FOUND", HttpStatus.NOT_FOUND);
	}
}