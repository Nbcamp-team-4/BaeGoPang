package com._team._project.domain.order.service;

import java.util.List;
import java.util.UUID;

import com._team._project.domain.order.api.request.CancelOrderRequest;
import com._team._project.domain.order.api.request.UpdateOrderStatusRequest;
import com._team._project.domain.order.api.response.CancelOrderResponse;
import com._team._project.domain.order.api.response.GetOrderDetailResponse;
import com._team._project.domain.order.api.response.GetOrderSummaryResponse;
import com._team._project.domain.order.api.response.UpdateOrderStatusResponse;

public interface OrderAdminService {

    // 관리자: 주문 전체 조회
    List<GetOrderSummaryResponse> getAllOrders();

    // 관리자: 주문 상세 조회
    GetOrderDetailResponse getOrderDetail(UUID orderId);

    // 관리자: 주문 강제 취소
    CancelOrderResponse cancelOrder(UUID orderId, CancelOrderRequest request);

    // 관리자: 주문 상태 강제 변경
    UpdateOrderStatusResponse updateOrderStatus(UUID orderId, UpdateOrderStatusRequest request);
}