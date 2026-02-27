package com._team._project.domain.pg_provider.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com._team._project.domain.pg_provider.exception.DuplicatePgProviderCodeException;
import com._team._project.global.common.dto.BaseResponse;

@RestControllerAdvice(basePackages = "com._team._project.domain.pg_provider")
public class PgProviderExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BaseResponse<Void>> handleNotFound(MethodArgumentNotValidException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.ofError(e.getMessage()));
	}

	@ExceptionHandler(DuplicatePgProviderCodeException.class)
	public ResponseEntity<BaseResponse<Void>> handlePgProviderAlreadyExists(DuplicatePgProviderCodeException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(BaseResponse.ofError(e.getErrorCode()));
	}
}