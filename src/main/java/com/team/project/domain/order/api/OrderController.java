package com.team.project.domain.order.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.order.api.request.CancelOrderRequest;
import com.team.project.domain.order.api.request.CreateOrderRequest;
import com.team.project.domain.order.api.request.UpdateOrderStatusRequest;
import com.team.project.domain.order.api.response.CancelOrderResponse;
import com.team.project.domain.order.api.response.CreateOrderResponse;
import com.team.project.domain.order.api.response.GetOrderDetailResponse;
import com.team.project.domain.order.api.response.GetOrderSummaryResponse;
import com.team.project.domain.order.api.response.UpdateOrderStatusResponse;
import com.team.project.domain.order.service.OrderService;
import com.team.project.global.common.dto.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * [고객] 주문 생성 (장바구니 → 주문 전환)
     * POST /api/orders
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        CreateOrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [고객] 내 주문 목록 조회
     * GET /api/orders
     * - 인증 적용 전 임시로 userId를 query param으로 받음
     */
    @GetMapping
    public ResponseEntity<?> getMyOrders(@RequestParam(value = "userId", required = false) UUID userId,
                                         @RequestParam(value = "storeId", required = false) UUID storeId) {

        // 가게 주문 목록 조회
        if (storeId != null) {
            List<GetOrderSummaryResponse> response = orderService.getStoreOrders(storeId);
            return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
        }

        // 고객 주문 목록 조회
        if (userId != null) {
            List<GetOrderSummaryResponse> response = orderService.getMyOrders(userId);
            return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
        }

        // 둘 다 없으면 잘못된 요청
        throw new IllegalArgumentException("userId 또는 storeId 중 하나는 반드시 전달해야 합니다.");
    }

    /**
     * [고객] 내 주문 상세 조회
     * GET /api/orders/{orderId}
     * - 인증 적용 전 임시로 userId를 query param으로 받음
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getMyOrderDetail(@PathVariable("orderId") UUID orderId,
                                              @RequestParam(value = "userId", required = false) UUID userId,
                                              @RequestParam(value = "storeId", required = false) UUID storeId) {

        // 고객/가게 상세를 같은 URL에서 분기 (API 명세 기준)
        if (storeId != null) {
            // [가게] 가게 주문 상세 조회
            // GET /api/orders/{orderId}?storeId={storeId}
            // TODO: manager 권한 체크
            GetOrderDetailResponse response = orderService.getStoreOrderDetail(orderId, storeId);
            return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
        }

        // [고객] 내 주문 상세 조회
        // GET /api/orders/{orderId}
        // TODO: 인증 붙이면 userId는 SecurityContext로 대체
        GetOrderDetailResponse response = orderService.getOrderDetail(orderId, userId);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [고객] 주문 취소
     * PUT /api/orders/{orderId}/cancel
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable("orderId") UUID orderId,
                                         @RequestParam("userId") UUID userId,
                                         @RequestBody(required = false) @Valid CancelOrderRequest request) {

        CancelOrderResponse response = orderService.cancelOrder(orderId, userId, request);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [가게] 주문 수락
     * PUT /api/orders/{orderId}/accept?storeId={storeId}
     */
    @PutMapping("/{orderId}/accept")
    public ResponseEntity<?> acceptOrder(@PathVariable("orderId") UUID orderId,
                                         @RequestParam("storeId") UUID storeId) {

        // TODO: manager 권한 체크
        UpdateOrderStatusResponse response = orderService.acceptOrder(orderId, storeId);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [가게] 주문 거절
     * PUT /api/orders/{orderId}/reject?storeId={storeId}
     */
    @PutMapping("/{orderId}/reject")
    public ResponseEntity<?> rejectOrder(@PathVariable("orderId") UUID orderId,
                                         @RequestParam("storeId") UUID storeId,
                                         @RequestBody(required = false) CancelOrderRequest request) {

        // TODO: manager 권한 체크
        // 거절 사유를 재사용하려면 CancelOrderRequest 같은 형태로 reason을 받을 수 있음(선택)
        String reason = (request == null) ? null : request.getReason();

        UpdateOrderStatusResponse response = orderService.rejectOrder(orderId, storeId, reason);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [가게] 주문 상태 변경
     * PUT /api/orders/{orderId}/status?storeId={storeId}
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatusByStore(@PathVariable("orderId") UUID orderId,
                                                      @RequestParam("storeId") UUID storeId,
                                                      @RequestBody @Valid UpdateOrderStatusRequest request) {

        // TODO: manager 권한 체크
        UpdateOrderStatusResponse response = orderService.updateOrderStatusByStore(orderId, storeId, request);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }
}