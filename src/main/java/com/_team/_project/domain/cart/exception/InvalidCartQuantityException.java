package com._team._project.domain.cart.exception;

import com._team._project.global.common.exception.BaseException;

/** 장바구니 수량이 유효하지 않을 때 발생(예: 1 미만) */
public class InvalidCartQuantityException extends BaseException {
    public InvalidCartQuantityException() {
        super(CartErrorCode.INVALID_CART_QUANTITY.getCode());
    }
}