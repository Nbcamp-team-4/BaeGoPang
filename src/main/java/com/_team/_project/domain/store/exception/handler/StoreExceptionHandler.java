package com._team._project.domain.store.exception.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com._team._project.domain.store.exception.InvalidDeliveryFeeException;
import com._team._project.domain.store.exception.InvalidDeliveryTimeException;
import com._team._project.domain.store.exception.InvalidMinimumOrderAmountException;
import com._team._project.domain.store.exception.InvalidStoreRequestException;
import com._team._project.domain.store.exception.InvalidStoreTimeException;
import com._team._project.domain.store.exception.StoreForbiddenException;
import com._team._project.domain.store.exception.StoreNotFoundException;
import com._team._project.domain.store.exception.StoreNotOperatingException;

@RestControllerAdvice(basePackages = "com._team._project.domain.store")
public class StoreExceptionHandler {

	@ExceptionHandler(StoreNotFoundException.class)
	public ResponseEntity<?> handleStoreNotFound(StoreNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(Map.of(
				"status", 404,
				"error", "STORE_NOT_FOUND",
				"message", e.getMessage()
			));
	}

	@ExceptionHandler(StoreForbiddenException.class)
	public ResponseEntity<?> handleStoreForbidden(StoreForbiddenException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
			.body(Map.of(
				"status", 403,
				"error", "STORE_FORBIDDEN",
				"message", e.getMessage()
			));
	}

	@ExceptionHandler(StoreNotOperatingException.class)
	public ResponseEntity<?> handleStoreNotOperating(StoreNotOperatingException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(Map.of(
				"status", 400,
				"error", "STORE_NOT_OPERATING",
				"message", e.getMessage()
			));
	}

	@ExceptionHandler({
		InvalidDeliveryFeeException.class,
		InvalidDeliveryTimeException.class,
		InvalidStoreTimeException.class,
		InvalidMinimumOrderAmountException.class,
		InvalidStoreRequestException.class
	})
	public ResponseEntity<?> handleBadRequest(RuntimeException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(Map.of(
				"status", 400,
				"error", "INVALID_STORE_REQUEST",
				"message", e.getMessage()
			));
	}
}