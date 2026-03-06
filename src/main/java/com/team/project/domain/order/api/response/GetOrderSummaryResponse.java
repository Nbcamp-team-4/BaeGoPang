package com.team.project.domain.order.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.model.vo.OrderStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetOrderSummaryResponse {

    private UUID id;
    private String orderNo;
    private OrderStatus status;

    private UUID storeId;
    private Integer totalAmount;
    private LocalDateTime orderDate;

    public static GetOrderSummaryResponse from(Order order) {
        return GetOrderSummaryResponse.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .status(order.getStatus())
                .storeId(order.getStore().getId())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .build();
    }
}