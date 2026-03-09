package com.team.project.domain.store.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class InvalidStoreRequestException extends BaseException {

	public InvalidStoreRequestException() {
		super(StoreErrorCode.INVALID_STORE_REQUEST.name(), HttpStatus.BAD_REQUEST);
	}
}