package com.team.project.domain.order.exception;

public class OrderPaymentCancelFailedException extends RuntimeException {
    public OrderPaymentCancelFailedException(String message) {
        super(message);
    }
}