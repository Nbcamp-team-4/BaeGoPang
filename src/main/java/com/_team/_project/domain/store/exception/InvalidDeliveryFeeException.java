package com._team._project.domain.store.exception;

public class InvalidDeliveryFeeException extends RuntimeException {

	public InvalidDeliveryFeeException() {
		super(StoreErrorCode.INVALID_DELIVERY_FEE.getMessage());
	}
}