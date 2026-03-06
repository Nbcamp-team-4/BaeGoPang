package com.team.project.domain.order.exception;

import com.team.project.global.common.exception.BaseException;

/** 본인 주문이 아니거나 권한이 없을 때 발생 */
public class OrderForbiddenException extends BaseException {
    public OrderForbiddenException() {
        super(OrderErrorCode.ORDER_FORBIDDEN.getCode());
    }
}