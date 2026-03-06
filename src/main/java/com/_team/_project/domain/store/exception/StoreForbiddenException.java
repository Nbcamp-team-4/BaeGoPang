package com._team._project.domain.store.exception;

public class StoreForbiddenException extends RuntimeException {

	public StoreForbiddenException() {
		super(StoreErrorCode.STORE_FORBIDDEN.getMessage());
	}
}