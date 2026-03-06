package com.team.project.domain.store.exception;

public class InvalidDeliveryTimeException extends RuntimeException {

	public InvalidDeliveryTimeException() {
		super(StoreErrorCode.INVALID_DELIVERY_TIME.getMessage());
	}
}