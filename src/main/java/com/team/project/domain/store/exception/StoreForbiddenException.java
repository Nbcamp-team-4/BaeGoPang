package com.team.project.domain.store.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class StoreForbiddenException extends BaseException {

	public StoreForbiddenException() {
		super(StoreErrorCode.STORE_FORBIDDEN.name(), HttpStatus.FORBIDDEN);
	}
}