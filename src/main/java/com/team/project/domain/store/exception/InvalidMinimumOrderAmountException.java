package com.team.project.domain.store.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class InvalidMinimumOrderAmountException extends BaseException {

	public InvalidMinimumOrderAmountException() {
		super(StoreErrorCode.INVALID_MINIMUM_ORDER_AMOUNT.name(), HttpStatus.BAD_REQUEST);
	}
}