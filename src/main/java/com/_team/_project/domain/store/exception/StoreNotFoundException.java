package com._team._project.domain.store.exception;

public class StoreNotFoundException extends RuntimeException {

	public StoreNotFoundException() {
		super(StoreErrorCode.STORE_NOT_FOUND.getMessage());
	}
}