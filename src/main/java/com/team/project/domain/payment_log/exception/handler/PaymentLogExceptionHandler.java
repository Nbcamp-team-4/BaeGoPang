package com.team.project.domain.payment_log.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.team.project.domain.payment_log.exception.PaymentLogNotFoundException;
import com.team.project.domain.pg_provider.exception.InvalidInputException;
import com.team.project.global.common.dto.BaseResponse;

@RestControllerAdvice(basePackages = "com._team._project.domain.payment_log")
public class PaymentLogExceptionHandler {

	@ExceptionHandler(PaymentLogNotFoundException.class)
	public ResponseEntity<BaseResponse<Void>> handleNotFound(PaymentLogNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.ofError(e.getMessage()));
	}

	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<BaseResponse<Void>> handleInvalidInput(InvalidInputException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.ofError(e.getErrorCode()));
	}

}
