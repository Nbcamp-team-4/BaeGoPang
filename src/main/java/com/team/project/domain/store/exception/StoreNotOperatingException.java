package com.team.project.domain.store.exception;

public class StoreNotOperatingException extends RuntimeException {

	public StoreNotOperatingException() {
		super(StoreErrorCode.STORE_NOT_OPERATING.getMessage());
	}
}