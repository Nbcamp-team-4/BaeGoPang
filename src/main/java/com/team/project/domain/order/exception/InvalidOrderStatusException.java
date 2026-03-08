package com.team.project.domain.order.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

/** 주문 상태가 현재 작업과 맞지 않아 처리할 수 없을 때 발생(상태 전이 불가) */
public class InvalidOrderStatusException extends BaseException {
	public InvalidOrderStatusException() {
		super(OrderErrorCode.INVALID_ORDER_STATUS.getCode(), HttpStatus.BAD_REQUEST);
	}
}