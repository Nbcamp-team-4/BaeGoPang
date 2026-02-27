package com._team._project.domain.pg_provider.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com._team._project.domain.pg_provider.exception.AlreadyDeactivatedPgProviderException;
import com._team._project.domain.pg_provider.exception.DuplicatePgProviderCodeException;
import com._team._project.domain.pg_provider.exception.PgProviderNotFoundException;
import com._team._project.global.common.dto.BaseResponse;

@RestControllerAdvice(basePackages = "com._team._project.domain.pg_provider")
public class PgProviderExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BaseResponse<Void>> handleNotFound(MethodArgumentNotValidException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.ofError(e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<BaseResponse<Void>> handleNotFound(MethodArgumentTypeMismatchException e) {
		String field = e.getName().toUpperCase();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.ofError(field + "_TYPE_MISMATCH"));
	}

	@ExceptionHandler(DuplicatePgProviderCodeException.class)
	public ResponseEntity<BaseResponse<Void>> handlePgProviderAlreadyExists(DuplicatePgProviderCodeException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(BaseResponse.ofError(e.getErrorCode()));
	}

	@ExceptionHandler(PgProviderNotFoundException.class)
	public ResponseEntity<BaseResponse<Void>> handlePgProviderAlreadyExists(PgProviderNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.ofError(e.getErrorCode()));
	}

	@ExceptionHandler(AlreadyDeactivatedPgProviderException.class)
	public ResponseEntity<BaseResponse<Void>> handleAlreadyDeactivatedPgProvider(
		AlreadyDeactivatedPgProviderException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.ofError(e.getErrorCode()));
	}
}