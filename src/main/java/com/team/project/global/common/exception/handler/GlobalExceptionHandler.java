package com.team.project.global.common.exception.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
	 //  * 모든 Exception 처리
	 //  */
	// @ExceptionHandler(Exception.class)
	// public ResponseEntity<BaseResponse<Void>> handleException(Exception e) {
	// 	log.error(e.getMessage(), e);
	// 	return ResponseEntity.internalServerError().body(
	// 		BaseResponse.ofError("INTERNAL_SERVER_ERROR")
	// 	);
	// }
	//
	// /**
	//  * Base Exception 처리(서버 커스텀 런타임 예외)
	//  */
	// @ExceptionHandler(BaseException.class)
	// public ResponseEntity<BaseResponse<?>> handleBaseException(BaseException e) {
	// 	log.error(e.getMessage(), e);
	// 	return ResponseEntity.status(e.getHttpStatus()).body(
	// 		BaseResponse.ofError(e.getErrorCode())
	// 	);
	// }
	//
	// /**
	//  * 요청 Dto 검증 Exception 처리
	//  */
	// @ExceptionHandler(MethodArgumentNotValidException.class)
	// public ResponseEntity<BaseResponse<Void>> handleNotFound(MethodArgumentNotValidException e) {
	// 	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.ofError(e.getMessage()));
	// }
	//
	// /**
	//  * 요청 Dto Type Exception 처리
	//  */
	// @ExceptionHandler(MethodArgumentTypeMismatchException.class)
	// public ResponseEntity<BaseResponse<Void>> handleNotFound(MethodArgumentTypeMismatchException e) {
	// 	String field = e.getName().toUpperCase();
	// 	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.ofError(field + "_TYPE_MISMATCH"));
	// }
}