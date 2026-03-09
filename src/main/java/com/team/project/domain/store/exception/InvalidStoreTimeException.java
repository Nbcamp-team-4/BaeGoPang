package com.team.project.domain.store.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class InvalidStoreTimeException extends BaseException {

	public InvalidStoreTimeException() {
		super(StoreErrorCode.INVALID_STORE_TIME.name(), HttpStatus.BAD_REQUEST);
	}
}