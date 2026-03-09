package com.team.project.domain.cart.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CartErrorCode {

    // 장바구니 자체
    CART_NOT_FOUND("CART_NOT_FOUND"),                 // 장바구니를 찾을 수 없음
    CART_ITEM_NOT_FOUND("CART_ITEM_NOT_FOUND"),       // 장바구니 아이템을 찾을 수 없음
    CART_FORBIDDEN("CART_FORBIDDEN"),                 // 본인 장바구니가 아니거나 접근 권한 없음

    // 정책/상태 관련
    INVALID_CART_STATUS("INVALID_CART_STATUS"),       // 현재 장바구니 상태에서 작업 불가(ORDERED/ABANDONED 등)
    INVALID_CART_QUANTITY("INVALID_CART_QUANTITY"),   // 수량이 유효하지 않음(1 미만 등)

    // FK/참조 관련
    USER_NOT_FOUND("USER_NOT_FOUND"),                 // 유저를 찾을 수 없음
    STORE_NOT_FOUND("STORE_NOT_FOUND"),               // 가게를 찾을 수 없음
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND"),           // 상품을 찾을 수 없음
    PRODUCT_OPTION_NOT_FOUND("PRODUCT_OPTION_NOT_FOUND"),         // 상품 옵션을 찾을 수 없음
    PRODUCT_OPTION_ITEM_NOT_FOUND("PRODUCT_OPTION_ITEM_NOT_FOUND"); // 옵션 항목을 찾을 수 없음

    private final String code;
}