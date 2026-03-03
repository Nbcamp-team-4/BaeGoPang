package com._team._project.domain.cart.exception;

import com._team._project.global.common.exception.BaseException;

/** 장바구니 아이템을 찾을 수 없을 때 발생 */
public class CartItemNotFoundException extends BaseException {
    public CartItemNotFoundException() {
        super(CartErrorCode.CART_ITEM_NOT_FOUND.getCode());
    }
}