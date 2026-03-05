package com._team._project.domain.order.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com._team._project.domain.order.entity.Order;
import com._team._project.domain.order.model.vo.OrderStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateOrderStatusResponse {

    private UUID id;
    private String orderNo;
    private OrderStatus status;

    private LocalDateTime updatedAt;
    private UUID updatedBy;

    public static UpdateOrderStatusResponse from(Order order) {
        return UpdateOrderStatusResponse.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .status(order.getStatus())
                .updatedAt(order.getUpdatedAt())
                .updatedBy(order.getUpdatedBy())
                .build();
    }
}