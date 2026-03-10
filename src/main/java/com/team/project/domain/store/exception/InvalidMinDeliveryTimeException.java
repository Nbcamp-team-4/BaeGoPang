package com.team.project.domain.store.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class InvalidMinDeliveryTimeException extends BaseException {

	public InvalidMinDeliveryTimeException() {
		super(StoreErrorCode.INVALID_MIN_DELIVERY_TIME.name(), HttpStatus.BAD_REQUEST);
	}
}