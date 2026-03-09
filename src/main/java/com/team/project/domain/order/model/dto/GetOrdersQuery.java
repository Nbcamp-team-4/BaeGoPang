package com.team.project.domain.order.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.team.project.domain.order.model.vo.OrderStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetOrdersQuery {

    private List<Item> content;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;

    @Getter
    @Builder
    public static class Item {
        private UUID id;
        private String orderNo;
        private OrderStatus status;
        private Integer totalAmount;
        private LocalDateTime createdAt;
        private UUID storeId;
        private String storeName;
        private UUID userId;
        private String userName;
    }
}