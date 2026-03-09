package com.team.project.domain.order.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.global.common.dto.BaseRangeRequest;

import lombok.Getter;

@Getter
public class GetOrdersCommand {

    private final Integer page;
    private final Integer size;
    private final OrderStatus status;
    private final BaseRangeRequest<LocalDateTime> rangeCreatedAt;
    private final UUID storeId;
    private final UUID userId;

    public GetOrdersCommand(
            Integer page,
            Integer size,
            OrderStatus status,
            BaseRangeRequest<LocalDateTime> rangeCreatedAt,
            UUID storeId,
            UUID userId
    ) {
        this.page = page;
        this.size = size;
        this.status = status;
        this.rangeCreatedAt = rangeCreatedAt;
        this.storeId = storeId;
        this.userId = userId;
    }

    public static GetOrdersCommand of(
            Integer page,
            Integer size,
            OrderStatus status,
            BaseRangeRequest<LocalDateTime> rangeCreatedAt,
            UUID storeId,
            UUID userId
    ) {
        return new GetOrdersCommand(page, size, status, rangeCreatedAt, storeId, userId);
    }
}