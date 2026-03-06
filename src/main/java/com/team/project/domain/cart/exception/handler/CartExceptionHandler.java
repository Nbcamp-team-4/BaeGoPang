package com.team.project.domain.cart.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.team.project.domain.cart.exception.CartForbiddenException;
import com.team.project.domain.cart.exception.CartItemNotFoundException;
import com.team.project.domain.cart.exception.CartNotFoundException;
import com.team.project.domain.cart.exception.InvalidCartQuantityException;
import com.team.project.domain.cart.exception.InvalidCartStatusException;
import com.team.project.global.common.dto.BaseResponse;

@RestControllerAdvice(basePackages = "com._team._project.domain.cart")
public class CartExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
        // 요청 DTO 검증 실패(@NotNull 등) 처리
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.ofError(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        // UUID 타입 등 파라미터 타입이 맞지 않을 때 처리
        String field = e.getName().toUpperCase();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.ofError(field + "_TYPE_MISMATCH"));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleCartNotFound(CartNotFoundException e) {
        // 장바구니 조회 실패
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.ofError(e.getErrorCode()));
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleCartItemNotFound(CartItemNotFoundException e) {
        // 장바구니 아이템 조회 실패
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.ofError(e.getErrorCode()));
    }

    @ExceptionHandler(CartForbiddenException.class)
    public ResponseEntity<BaseResponse<Void>> handleCartForbidden(CartForbiddenException e) {
        // 권한 없음(본인 장바구니 아님)
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(BaseResponse.ofError(e.getErrorCode()));
    }

    @ExceptionHandler(InvalidCartStatusException.class)
    public ResponseEntity<BaseResponse<Void>> handleInvalidCartStatus(InvalidCartStatusException e) {
        // 상태가 ORDERED/ABANDONED 등이라 작업 불가
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.ofError(e.getErrorCode()));
    }

    @ExceptionHandler(InvalidCartQuantityException.class)
    public ResponseEntity<BaseResponse<Void>> handleInvalidCartQuantity(InvalidCartQuantityException e) {
        // 수량이 1 미만 등 유효하지 않을 때
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.ofError(e.getErrorCode()));
    }
}