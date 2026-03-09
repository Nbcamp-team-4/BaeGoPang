package com.team.project.domain.order.service;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.order.api.request.CancelOrderRequest;
import com.team.project.domain.order.api.request.ConfirmOrderPaymentRequest;
import com.team.project.domain.order.api.request.CreateOrderRequest;
import com.team.project.domain.order.api.request.UpdateOrderStatusRequest;
import com.team.project.domain.order.api.response.CancelOrderResponse;
import com.team.project.domain.order.api.response.ConfirmOrderPaymentResponse;
import com.team.project.domain.order.api.response.CreateOrderResponse;
import com.team.project.domain.order.api.response.GetOrderDetailResponse;
import com.team.project.domain.order.api.response.GetOrderSummaryResponse;
import com.team.project.domain.order.api.response.UpdateOrderStatusResponse;

public interface OrderService {

    // ======================
    // customer
    // ======================

    CreateOrderResponse createOrder(UUID userId, CreateOrderRequest request);

    List<GetOrderSummaryResponse> getMyOrders(UUID userId);

    GetOrderDetailResponse getOrderDetail(UUID orderId, UUID userId);

    CancelOrderResponse cancelOrder(UUID orderId, UUID userId, CancelOrderRequest request);

    void deleteOrder(UUID orderId, UUID userId);

    ConfirmOrderPaymentResponse confirmOrderPayment(UUID userId, UUID orderId, ConfirmOrderPaymentRequest request);

    // ======================
    // owner (store)
    // ======================

    List<GetOrderSummaryResponse> getStoreOrders(UUID ownerUserId, UUID storeId);

    GetOrderDetailResponse getStoreOrderDetail(UUID ownerUserId, UUID orderId, UUID storeId);

    UpdateOrderStatusResponse acceptOrder(UUID ownerUserId, UUID orderId, UUID storeId);

    UpdateOrderStatusResponse rejectOrder(UUID ownerUserId, UUID orderId, UUID storeId, String reason);

    UpdateOrderStatusResponse updateOrderStatusByStore(UUID ownerUserId, UUID orderId, UUID storeId,
                                                       UpdateOrderStatusRequest request);
}