package com.team.project.domain.order.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.model.vo.OrderStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CancelOrderResponse {

    private UUID id;
    private String orderNo;
    private OrderStatus status;

    private String canceledReason;

    private LocalDateTime updatedAt;
    private UUID updatedBy;

    public static CancelOrderResponse from(Order order) {
        return CancelOrderResponse.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .status(order.getStatus())
                .canceledReason(order.getCanceledReason())
                .updatedAt(order.getUpdatedAt())
                .updatedBy(order.getUpdatedBy())
                .build();
    }
}