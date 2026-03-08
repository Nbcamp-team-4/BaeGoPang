package com.team.project.domain.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode {

    // 400: 요청값/상태값이 잘못됨
    INVALID_ORDER_STATUS("INVALID_ORDER_STATUS"),     // 주문 상태가 올바르지 않음(전이 불가 등)
    INVALID_ORDER_REQUEST("INVALID_ORDER_REQUEST"),   // 주문 요청 값이 유효하지 않음(필수값 누락/형식 오류 등)

    // 403: 권한/소유권 문제
    ORDER_FORBIDDEN("ORDER_FORBIDDEN"),               // 본인 주문이 아니거나 권한이 없어 접근 불가

    // 404: 리소스 없음
    ORDER_NOT_FOUND("ORDER_NOT_FOUND"),               // 주문을 찾을 수 없음
    ORDER_ITEM_NOT_FOUND("ORDER_ITEM_NOT_FOUND"),     // 주문 상품(아이템)을 찾을 수 없음

    // 409: 현재 상태에서 작업 충돌
    ORDER_ALREADY_CANCELED("ORDER_ALREADY_CANCELED"), // 이미 취소된 주문
    ORDER_CANNOT_CANCEL("ORDER_CANNOT_CANCEL");       // 현재 상태에서는 주문 취소 불가(완료/취소 등)

    private final String code;
}