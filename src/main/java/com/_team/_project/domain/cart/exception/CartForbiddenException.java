package com._team._project.domain.cart.exception;

import com._team._project.global.common.exception.BaseException;

/** 본인 장바구니가 아니거나 권한이 없을 때 발생 */
public class CartForbiddenException extends BaseException {
    public CartForbiddenException() {
        super(CartErrorCode.CART_FORBIDDEN.getCode());
    }
}