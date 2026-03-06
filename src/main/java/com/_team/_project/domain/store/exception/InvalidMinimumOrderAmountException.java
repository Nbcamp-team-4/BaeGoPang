package com._team._project.domain.store.exception;

public class InvalidMinimumOrderAmountException extends RuntimeException {

	public InvalidMinimumOrderAmountException() {
		super(StoreErrorCode.INVALID_MINIMUM_ORDER_AMOUNT.getMessage());
	}
}