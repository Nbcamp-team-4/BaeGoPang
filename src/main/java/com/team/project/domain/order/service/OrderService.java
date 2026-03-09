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
import com.team.project.domain.order.model.dto.GetOrdersCommand;
import com.team.project.domain.order.model.dto.GetOrdersQuery;

public interface OrderService {

    // ======================
    // customer
    // ======================

    CreateOrderResponse createOrder(UUID userId, CreateOrderRequest request);

    GetOrdersQuery getMyOrders(UUID userId, GetOrdersCommand command);

    GetOrderDetailResponse getOrderDetail(UUID orderId, UUID userId);

    CancelOrderResponse cancelOrder(UUID orderId, UUID userId, CancelOrderRequest request);

    void deleteOrder(UUID orderId, UUID userId);

    ConfirmOrderPaymentResponse confirmOrderPayment(UUID userId, UUID orderId, ConfirmOrderPaymentRequest request);

    // ======================
    // owner (store)
    // ======================

    GetOrdersQuery getStoreOrders(UUID ownerUserId, UUID storeId, GetOrdersCommand command);

    GetOrderDetailResponse getStoreOrderDetail(UUID ownerUserId, UUID orderId, UUID storeId);

    UpdateOrderStatusResponse acceptOrder(UUID ownerUserId, UUID orderId, UUID storeId);

    UpdateOrderStatusResponse rejectOrder(UUID ownerUserId, UUID orderId, UUID storeId, String reason);

    UpdateOrderStatusResponse updateOrderStatusByStore(UUID ownerUserId, UUID orderId, UUID storeId,
                                                       UpdateOrderStatusRequest request);
}