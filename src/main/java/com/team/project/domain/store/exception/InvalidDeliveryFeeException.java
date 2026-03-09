package com.team.project.domain.store.exception;

import org.springframework.http.HttpStatus;

import com.team.project.global.common.exception.BaseException;

import lombok.Getter;

@Getter
public class InvalidDeliveryFeeException extends BaseException {

	public InvalidDeliveryFeeException() {
		super(StoreErrorCode.INVALID_DELIVERY_FEE.name(), HttpStatus.BAD_REQUEST);
	}
}