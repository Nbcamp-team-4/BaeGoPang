package com._team._project.domain.cart.exception;

import com._team._project.global.common.exception.BaseException;

/** 장바구니를 찾을 수 없을 때 발생 */
public class CartNotFoundException extends BaseException {
    public CartNotFoundException() {
        super(CartErrorCode.CART_NOT_FOUND.getCode());
    }
}