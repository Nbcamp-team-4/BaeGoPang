package com.team.project.domain.store.exception;

public class InvalidStoreTimeException extends RuntimeException {

	public InvalidStoreTimeException() {
		super(StoreErrorCode.INVALID_STORE_TIME.getMessage());
	}
}