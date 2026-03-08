package com.team.project.domain.cart.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

/** 본인 장바구니가 아니거나 권한이 없을 때 발생 */
public class CartForbiddenException extends BaseException {
	public CartForbiddenException() {
		super(CartErrorCode.CART_FORBIDDEN.getCode(), HttpStatus.FORBIDDEN);
	}
}