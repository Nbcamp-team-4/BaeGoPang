package com._team._project.domain.order.api.request;

import javax.validation.constraints.NotNull;

import com._team._project.domain.order.model.vo.OrderStatus;

import lombok.Data;

@Data
public class UpdateOrderStatusRequest {

    @NotNull
    private OrderStatus status; // 예: PAID, COMPLETED
}