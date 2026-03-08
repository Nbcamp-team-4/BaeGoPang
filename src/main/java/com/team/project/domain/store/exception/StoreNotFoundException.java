package com.team.project.domain.store.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

public class StoreNotFoundException extends BaseException {
	public StoreNotFoundException() {
		super("STORE_NOT_FOUND", HttpStatus.NOT_FOUND);
	}
}