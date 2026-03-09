package com.team.project.domain.order.service;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.order.api.request.CancelOrderRequest;
import com.team.project.domain.order.api.request.UpdateOrderStatusRequest;
import com.team.project.domain.order.api.response.CancelOrderResponse;
import com.team.project.domain.order.api.response.GetOrderDetailResponse;
import com.team.project.domain.order.api.response.GetOrderSummaryResponse;
import com.team.project.domain.order.api.response.UpdateOrderStatusResponse;
import com.team.project.domain.order.model.dto.GetOrdersCommand;
import com.team.project.domain.order.model.dto.GetOrdersQuery;

public interface OrderAdminService {

    // 관리자: 주문 전체 조회
    GetOrdersQuery getAllOrders(GetOrdersCommand command);

    // 관리자: 주문 상세 조회
    GetOrderDetailResponse getOrderDetail(UUID orderId);

    // 관리자: 주문 강제 취소
    CancelOrderResponse cancelOrder(UUID orderId, CancelOrderRequest request);

    // 관리자: 주문 상태 강제 변경
    UpdateOrderStatusResponse updateOrderStatus(UUID orderId, UpdateOrderStatusRequest request);
}