package com.team.project.domain.order.service;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.order.api.request.CancelOrderRequest;
import com.team.project.domain.order.api.request.ConfirmOrderPaymentRequest;
import com.team.project.domain.order.api.request.CreateOrderRequest;
import com.team.project.domain.order.api.request.UpdateOrderStatusRequest;
import com.team.project.domain.order.api.response.*;

public interface OrderService {

    // ======================
    // customer
    // ======================

    // 주문 생성 (장바구니 → 주문 전환은 추후 cart 기반으로 변경 가능)
    CreateOrderResponse createOrder(CreateOrderRequest request);

    // 내 주문 목록
    List<GetOrderSummaryResponse> getMyOrders(UUID userId);

    // 내 주문 상세
    GetOrderDetailResponse getOrderDetail(UUID orderId, UUID userId);

    // 주문 취소
    CancelOrderResponse cancelOrder(UUID orderId, UUID userId, CancelOrderRequest request);

    // 주문 삭제(소프트 삭제)
    void deleteOrder(UUID orderId, UUID userId);

    // 결제 성공 처리
    ConfirmOrderPaymentResponse confirmOrderPayment(UUID orderId, ConfirmOrderPaymentRequest request);

    // ======================
    // manager (store)
    // ======================

    // 가게 주문 목록
    List<GetOrderSummaryResponse> getStoreOrders(UUID storeId);

    // 가게 주문 상세
    GetOrderDetailResponse getStoreOrderDetail(UUID orderId, UUID storeId);

    // 주문 수락
    UpdateOrderStatusResponse acceptOrder(UUID orderId, UUID storeId);

    // 주문 거절
    UpdateOrderStatusResponse rejectOrder(UUID orderId, UUID storeId, String reason);

    // 주문 상태 변경
    UpdateOrderStatusResponse updateOrderStatusByStore(UUID orderId, UUID storeId, UpdateOrderStatusRequest request);
}