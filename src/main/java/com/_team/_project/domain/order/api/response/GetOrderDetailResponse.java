package com._team._project.domain.order.api.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com._team._project.domain.order.entity.Order;
import com._team._project.domain.order.model.vo.OrderStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetOrderDetailResponse {

    private UUID id;
    private String orderNo;
    private OrderStatus status;

    private UUID userId;
    private UUID storeId;
    private UUID deliveryAddressId;

    private Integer totalAmount;
    private String requestMemo;
    private String canceledReason;

    private LocalDateTime orderDate;
    private LocalDateTime completedAt;

    private LocalDateTime createdAt;
    private UUID createdBy;
    private LocalDateTime updatedAt;
    private UUID updatedBy;

    private List<OrderItemDetail> items;

    @Getter
    @Builder
    public static class OrderItemDetail {
        private UUID id;
        private UUID productId;
        private String productName;
        private Integer unitPrice;
        private Integer quantity;
        private Integer lineTotalAmount;
        private List<OrderItemOptionDetail> options;
    }

    @Getter
    @Builder
    public static class OrderItemOptionDetail {
        private UUID id;
        private String optionName;
        private String optionItemName;
        private Integer extraPrice;
    }

    public static GetOrderDetailResponse from(Order order) {
        return GetOrderDetailResponse.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .status(order.getStatus())
                .userId(order.getUser().getId())
                .storeId(order.getStore().getId())
                .deliveryAddressId(order.getDeliveryAddress() == null ? null : order.getDeliveryAddress().getId())
                .totalAmount(order.getTotalAmount())
                .requestMemo(order.getRequestMemo())
                .canceledReason(order.getCanceledReason())
                .orderDate(order.getOrderDate())
                .completedAt(order.getCompletedAt())
                .createdAt(order.getCreatedAt())
                .createdBy(order.getCreatedBy())
                .updatedAt(order.getUpdatedAt())
                .updatedBy(order.getUpdatedBy())
                .items(order.getItems().stream()
                        .map(i -> OrderItemDetail.builder()
                                .id(i.getId())
                                .productId(i.getProduct().getId())
                                .productName(i.getProductName())
                                .unitPrice(i.getUnitPrice())
                                .quantity(i.getQuantity())
                                .lineTotalAmount(i.getLineTotalAmount())
                                .options(i.getOptions().stream()
                                        .map(o -> OrderItemOptionDetail.builder()
                                                .id(o.getId())
                                                .optionName(o.getOptionName())
                                                .optionItemName(o.getOptionItemName())
                                                .extraPrice(o.getExtraPrice())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}