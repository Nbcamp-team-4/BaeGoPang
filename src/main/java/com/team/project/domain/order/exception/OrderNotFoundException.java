package com.team.project.domain.order.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

/** 주문이 존재하지 않을 때 발생 */
public class OrderNotFoundException extends BaseException {
	public OrderNotFoundException() {
		super(OrderErrorCode.ORDER_NOT_FOUND.getCode(), HttpStatus.NOT_FOUND);
	}
}