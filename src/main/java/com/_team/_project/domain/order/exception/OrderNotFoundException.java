package com._team._project.domain.order.exception;

import com._team._project.global.common.exception.BaseException;

/** 주문이 존재하지 않을 때 발생 */
public class OrderNotFoundException extends BaseException {
    public OrderNotFoundException() {
        super(OrderErrorCode.ORDER_NOT_FOUND.getCode());
    }
}