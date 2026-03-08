package com.team.project.domain.store.exception;

public class InvalidStoreRequestException extends RuntimeException {

	public InvalidStoreRequestException() {
		super(StoreErrorCode.INVALID_STORE_REQUEST.getMessage());
	}
}