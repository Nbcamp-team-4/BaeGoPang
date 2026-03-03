package com._team._project.domain.cart.exception;

import com._team._project.global.common.exception.BaseException;

/** 현재 장바구니 상태(ORDERED/ABANDONED 등)에서 수행할 수 없는 작업일 때 발생 */
public class InvalidCartStatusException extends BaseException {
    public InvalidCartStatusException() {
        super(CartErrorCode.INVALID_CART_STATUS.getCode());
    }
}