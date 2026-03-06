package com.team.project.domain.store.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.team.project.domain.store.exception.StoreNotFoundException;

@RestControllerAdvice
public class StoreExceptionHandler {

	@ExceptionHandler(StoreNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleStoreNotFound(StoreNotFoundException e) {
		return e.getMessage();
	}
}