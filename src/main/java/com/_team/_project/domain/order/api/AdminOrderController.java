package com._team._project.domain.order.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com._team._project.domain.order.api.request.CancelOrderRequest;
import com._team._project.domain.order.api.request.UpdateOrderStatusRequest;
import com._team._project.domain.order.api.response.CancelOrderResponse;
import com._team._project.domain.order.api.response.GetOrderDetailResponse;
import com._team._project.domain.order.api.response.GetOrderSummaryResponse;
import com._team._project.domain.order.api.response.UpdateOrderStatusResponse;
import com._team._project.domain.order.service.OrderAdminService;
import com._team._project.global.common.dto.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/admin/orders")
@Slf4j
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderAdminService orderAdminService;

    /**
     * [관리자] 주문 전체 조회
     * GET /api/admin/orders
     */
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        List<GetOrderSummaryResponse> response = orderAdminService.getAllOrders();
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [관리자] 주문 상세 조회
     * GET /api/admin/orders/{orderId}
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetail(@PathVariable("orderId") UUID orderId) {
        GetOrderDetailResponse response = orderAdminService.getOrderDetail(orderId);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [관리자] 주문 강제 취소
     * PUT /api/admin/orders/{orderId}/cancel
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrderByAdmin(@PathVariable("orderId") UUID orderId,
                                                @RequestBody @Valid CancelOrderRequest request) {

        CancelOrderResponse response = orderAdminService.cancelOrder(orderId, request);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [관리자] 주문 상태 강제 변경
     * PUT /api/admin/orders/{orderId}/status
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatusByAdmin(@PathVariable("orderId") UUID orderId,
                                                      @RequestBody @Valid UpdateOrderStatusRequest request) {

        UpdateOrderStatusResponse response = orderAdminService.updateOrderStatus(orderId, request);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }
}