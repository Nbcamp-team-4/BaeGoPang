package com.team.project.domain.store.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class InvalidDeliveryTimeException extends BaseException {

	public InvalidDeliveryTimeException() {
		super(StoreErrorCode.INVALID_DELIVERY_TIME.name(), HttpStatus.BAD_REQUEST);
	}
}