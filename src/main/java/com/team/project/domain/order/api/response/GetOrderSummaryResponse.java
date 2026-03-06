package com.team.project.domain.order.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetOrderSummaryResponse {

    private UUID id;
    private String orderNo;
    private OrderStatus status;

    private UUID storeId;

    // 결제 요약 정보
    private UUID paymentId;
    private PaymentStatus paymentStatus;

    private Integer totalAmount;
    private LocalDateTime orderDate;

    public static GetOrderSummaryResponse from(Order order, Payment payment) {
        return GetOrderSummaryResponse.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .status(order.getStatus())
                .storeId(order.getStore().getId())
                .paymentId(payment == null ? null : payment.getId())
                .paymentStatus(payment == null ? null : payment.getStatus())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .build();
    }
}