package com.team.project.domain.product.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class ProductOptionItemNotFoundException extends BaseException {

	public ProductOptionItemNotFoundException() {
		super(ProductErrorCode.PRODUCT_OPTION_ITEM_NOT_FOUND.name(), HttpStatus.NOT_FOUND);
	}
}