package com._team._project.domain.order.exception;

import com._team._project.global.common.exception.BaseException;

/** 현재 주문 상태에서는 취소할 수 없을 때 발생(예: COMPLETED 등) */
public class OrderCannotCancelException extends BaseException {
    public OrderCannotCancelException() {
        super(OrderErrorCode.ORDER_CANNOT_CANCEL.getCode());
    }
}