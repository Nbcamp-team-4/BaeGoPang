package com.team.project.domain.order.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

/** 이미 취소된 주문을 다시 취소하려 할 때 발생 */
public class OrderAlreadyCanceledException extends BaseException {
	public OrderAlreadyCanceledException() {
		super(OrderErrorCode.ORDER_ALREADY_CANCELED.getCode(), HttpStatus.CONFLICT);
	}
}