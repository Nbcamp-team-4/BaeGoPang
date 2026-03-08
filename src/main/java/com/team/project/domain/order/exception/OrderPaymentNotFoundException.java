package com.team.project.domain.order.exception;

public class OrderPaymentNotFoundException extends RuntimeException {
    public OrderPaymentNotFoundException() {
        super("주문에 연결된 결제 정보를 찾을 수 없습니다.");
    }
}