package com.team.project.domain.order.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.domain.payment.model.dto.PayPaymentQuery;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConfirmOrderPaymentResponse {

    private UUID orderId;
    private String orderNo;
    private OrderStatus orderStatus;

    private UUID paymentId;
    private PaymentStatus paymentStatus;
    private Integer amount;
    private LocalDateTime paidAt;

    public static ConfirmOrderPaymentResponse from(Order order, PayPaymentQuery paymentQuery) {
        return ConfirmOrderPaymentResponse.builder()
                .orderId(order.getId())
                .orderNo(order.getOrderNo())
                .orderStatus(order.getStatus())
                .paymentId(paymentQuery.getId())
                .paymentStatus(paymentQuery.getStatus())
                .amount(paymentQuery.getAmount())
                .paidAt(paymentQuery.getPaidAt())
                .build();
    }
}