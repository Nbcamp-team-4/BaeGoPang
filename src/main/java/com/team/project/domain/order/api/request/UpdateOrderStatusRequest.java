package com.team.project.domain.order.api.request;

import javax.validation.constraints.NotNull;

import com.team.project.domain.order.model.vo.OrderStatus;

import lombok.Data;

@Data
public class UpdateOrderStatusRequest {

    @NotNull
    private OrderStatus status; // 예: PAID, COMPLETED
}