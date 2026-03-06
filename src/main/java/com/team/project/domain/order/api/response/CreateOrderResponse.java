package com.team.project.domain.order.api.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.model.vo.OrderStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateOrderResponse {

    private UUID id;
    private String orderNo;
    private OrderStatus status;

    private UUID userId;
    private UUID storeId;
    private UUID deliveryAddressId;

    private Integer totalAmount;
    private String requestMemo;

    private LocalDateTime orderDate;
    private LocalDateTime createdAt;
    private UUID createdBy;

    private List<OrderItemSummary> items;

    @Getter
    @Builder
    public static class OrderItemSummary {
        private UUID id;
        private UUID productId;
        private String productName;
        private Integer unitPrice;
        private Integer quantity;
        private Integer lineTotalAmount;
    }

    public static CreateOrderResponse from(Order order) {
        return CreateOrderResponse.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .status(order.getStatus())
                .userId(order.getUser().getId())
                .storeId(order.getStore().getId())
                .deliveryAddressId(order.getDeliveryAddress() == null ? null : order.getDeliveryAddress().getId())
                .totalAmount(order.getTotalAmount())
                .requestMemo(order.getRequestMemo())
                .orderDate(order.getOrderDate())
                .createdAt(order.getCreatedAt())
                .createdBy(order.getCreatedBy())
                .items(order.getItems().stream()
                        .map(i -> OrderItemSummary.builder()
                                .id(i.getId())
                                .productId(i.getProduct().getId())
                                .productName(i.getProductName())
                                .unitPrice(i.getUnitPrice())
                                .quantity(i.getQuantity())
                                .lineTotalAmount(i.getLineTotalAmount())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}