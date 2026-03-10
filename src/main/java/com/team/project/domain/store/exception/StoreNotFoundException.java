package com.team.project.domain.store.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class StoreNotFoundException extends BaseException {

	public StoreNotFoundException() {
		super(StoreErrorCode.STORE_NOT_FOUND.name(), HttpStatus.NOT_FOUND);
	}
}