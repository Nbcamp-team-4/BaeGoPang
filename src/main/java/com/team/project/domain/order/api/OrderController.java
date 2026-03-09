package com.team.project.domain.order.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.auth.dto.CurrentUser;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.order.api.request.CancelOrderRequest;
import com.team.project.domain.order.api.request.ConfirmOrderPaymentRequest;
import com.team.project.domain.order.api.request.CreateOrderRequest;
import com.team.project.domain.order.api.response.CancelOrderResponse;
import com.team.project.domain.order.api.response.ConfirmOrderPaymentResponse;
import com.team.project.domain.order.api.response.CreateOrderResponse;
import com.team.project.domain.order.api.response.GetOrderDetailResponse;
import com.team.project.domain.order.api.response.GetOrderSummaryResponse;
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
     * [고객] 주문 생성
     * - 로그인한 사용자 기준으로 주문 생성
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createOrder(
            @CurrentUser UserDto userDto,
            @RequestBody @Valid CreateOrderRequest request
    ) {
        CreateOrderResponse response = orderService.createOrder(userDto.getId(), request);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [고객] 결제 성공 처리
     * - 로그인한 사용자 본인 주문만 결제 가능
     */
    @PostMapping("/{orderId}/payment/success")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> confirmOrderPayment(
            @CurrentUser UserDto userDto,
            @PathVariable("orderId") UUID orderId,
            @RequestBody @Valid ConfirmOrderPaymentRequest request
    ) {
        ConfirmOrderPaymentResponse response = orderService.confirmOrderPayment(userDto.getId(), orderId, request);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [고객] 내 주문 목록 조회
     * - 로그인 사용자 기준으로 조회
     */
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getMyOrders(@CurrentUser UserDto userDto) {
        List<GetOrderSummaryResponse> response = orderService.getMyOrders(userDto.getId());
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [고객] 내 주문 상세 조회
     * - 로그인 사용자 기준으로 조회
     */
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getMyOrderDetail(
            @CurrentUser UserDto userDto,
            @PathVariable("orderId") UUID orderId
    ) {
        GetOrderDetailResponse response = orderService.getOrderDetail(orderId, userDto.getId());
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [고객] 주문 취소
     * - 로그인한 사용자 본인 주문만 취소 가능
     */
    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> cancelOrder(
            @CurrentUser UserDto userDto,
            @PathVariable("orderId") UUID orderId,
            @RequestBody(required = false) @Valid CancelOrderRequest request
    ) {
        CancelOrderResponse response = orderService.cancelOrder(orderId, userDto.getId(), request);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }
}