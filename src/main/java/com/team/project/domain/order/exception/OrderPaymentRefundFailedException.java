package com.team.project.domain.order.exception;

public class OrderPaymentRefundFailedException extends RuntimeException {
    public OrderPaymentRefundFailedException(String message) {
        super(message);
    }
}