package com._team._project.domain.order.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com._team._project.domain.order.exception.InvalidOrderStatusException;
import com._team._project.domain.order.exception.OrderAlreadyCanceledException;
import com._team._project.domain.order.exception.OrderCannotCancelException;
import com._team._project.domain.order.exception.OrderForbiddenException;
import com._team._project.domain.order.exception.OrderNotFoundException;
import com._team._project.global.common.dto.BaseResponse;

@RestControllerAdvice(basePackages = "com._team._project.domain.order")
public class OrderExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
        // 보통은 fieldError 리스트를 만들어주는 게 좋은데, 일단 결제 예시처럼 message로 처리
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.ofError(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String field = e.getName().toUpperCase();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.ofError(field + "_TYPE_MISMATCH"));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleOrderNotFound(OrderNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.ofError(e.getErrorCode()));
    }

    @ExceptionHandler(OrderForbiddenException.class)
    public ResponseEntity<BaseResponse<Void>> handleOrderForbidden(OrderForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(BaseResponse.ofError(e.getErrorCode()));
    }

    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<BaseResponse<Void>> handleInvalidOrderStatus(InvalidOrderStatusException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.ofError(e.getErrorCode()));
    }

    @ExceptionHandler(OrderCannotCancelException.class)
    public ResponseEntity<BaseResponse<Void>> handleCannotCancel(OrderCannotCancelException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(BaseResponse.ofError(e.getErrorCode()));
    }

    @ExceptionHandler(OrderAlreadyCanceledException.class)
    public ResponseEntity<BaseResponse<Void>> handleAlreadyCanceled(OrderAlreadyCanceledException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(BaseResponse.ofError(e.getErrorCode()));
    }
}