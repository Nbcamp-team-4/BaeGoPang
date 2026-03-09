package com.team.project.domain.store.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class StoreNotOperatingException extends BaseException {

	public StoreNotOperatingException() {
		super(StoreErrorCode.STORE_NOT_OPERATING.name(), HttpStatus.BAD_REQUEST);
	}
}