package com.team.project.domain.payment.exception.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com._team._project.domain.payment")
public class PaymentExceptionHandler {

	// @ExceptionHandler(MethodArgumentNotValidException.class)
	// public ResponseEntity<BaseResponse<Void>> handleNotFound(MethodArgumentNotValidException e) {
	// 	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.ofError(e.getMessage()));
	// }
	//
	// @ExceptionHandler(MethodArgumentTypeMismatchException.class)
	// public ResponseEntity<BaseResponse<Void>> handleNotFound(MethodArgumentTypeMismatchException e) {
	// 	String field = e.getName().toUpperCase();
	// 	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.ofError(field + "_TYPE_MISMATCH"));
	// }
	//
	// @ExceptionHandler(InvalidPaymentRequestException.class)
	// public ResponseEntity<BaseResponse<Void>> handleInvalidPaymentMethod(InvalidPaymentRequestException e) {
	// 	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.ofError(e.getMessage()));
	// }
	//
	// @ExceptionHandler(PaymentNotFoundException.class)
	// public ResponseEntity<BaseResponse<Void>> handlePaymentNotFound(PaymentNotFoundException e) {
	// 	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.ofError(e.getMessage()));
	// }
}
