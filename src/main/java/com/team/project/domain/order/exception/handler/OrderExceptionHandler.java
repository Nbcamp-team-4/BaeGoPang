package com.team.project.domain.order.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.team.project.global.common.dto.BaseResponse;

@RestControllerAdvice(basePackages = "com.team.project.domain.order")
public class OrderExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BaseResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
		// 보통은 fieldError 리스트를 만들어주는 게 좋은데, 일단 결제 예시처럼 message로 처리
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(BaseResponse.ofError(e.getMessage()));
	}

}