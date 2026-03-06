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
public class CancelOrderResponse {

    // 주문 ID
    private UUID id;

    // 주문 번호
    private String orderNo;

    // 주문 상태
    private OrderStatus status;

    // 결제 ID
    private UUID paymentId;

    // 결제 상태
    private PaymentStatus paymentStatus;

    // 취소 사유
    private String canceledReason;

    // 수정일시
    private LocalDateTime updatedAt;

    // 수정자
    private UUID updatedBy;

    public static CancelOrderResponse from(Order order, Payment payment) {
        return CancelOrderResponse.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .status(order.getStatus())
                .paymentId(payment == null ? null : payment.getId())
                .paymentStatus(payment == null ? null : payment.getStatus())
                .canceledReason(order.getCanceledReason())
                .updatedAt(order.getUpdatedAt())
                .updatedBy(order.getUpdatedBy())
                .build();
    }
}