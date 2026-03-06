package com.team.project.domain.store.exception;

public class InvalidMinDeliveryTimeException extends RuntimeException {

	public InvalidMinDeliveryTimeException() {
		super(StoreErrorCode.INVALID_MIN_DELIVERY_TIME.getMessage());
	}
}