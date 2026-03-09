package com.team.project.domain.order.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.auth.dto.CurrentUser;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.order.api.request.CancelOrderRequest;
import com.team.project.domain.order.api.request.UpdateOrderStatusRequest;
import com.team.project.domain.order.api.response.GetOrderDetailResponse;
import com.team.project.domain.order.api.response.GetOrderSummaryResponse;
import com.team.project.domain.order.api.response.UpdateOrderStatusResponse;
import com.team.project.domain.order.service.OrderService;
import com.team.project.global.common.dto.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/owner/orders")
@Slf4j
@RequiredArgsConstructor
public class OwnerOrderController {

    private final OrderService orderService;

    /**
     * [사장] 가게 주문 목록 조회
     * - 로그인한 OWNER의 가게인지 검증 후 조회
     */
    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> getStoreOrders(
            @CurrentUser UserDto userDto,
            @RequestParam("storeId") UUID storeId
    ) {
        List<GetOrderSummaryResponse> response = orderService.getStoreOrders(userDto.getId(), storeId);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [사장] 가게 주문 상세 조회
     * - 로그인한 OWNER의 가게인지 검증 후 조회
     */
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> getStoreOrderDetail(
            @CurrentUser UserDto userDto,
            @PathVariable("orderId") UUID orderId,
            @RequestParam("storeId") UUID storeId
    ) {
        GetOrderDetailResponse response = orderService.getStoreOrderDetail(userDto.getId(), orderId, storeId);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [사장] 주문 수락
     * - 로그인한 OWNER의 가게 주문만 수락 가능
     */
    @PutMapping("/{orderId}/accept")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> acceptOrder(
            @CurrentUser UserDto userDto,
            @PathVariable("orderId") UUID orderId,
            @RequestParam("storeId") UUID storeId
    ) {
        UpdateOrderStatusResponse response = orderService.acceptOrder(userDto.getId(), orderId, storeId);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [사장] 주문 거절
     * - 로그인한 OWNER의 가게 주문만 거절 가능
     */
    @PutMapping("/{orderId}/reject")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> rejectOrder(
            @CurrentUser UserDto userDto,
            @PathVariable("orderId") UUID orderId,
            @RequestParam("storeId") UUID storeId,
            @RequestBody(required = false) CancelOrderRequest request
    ) {
        String reason = (request == null) ? null : request.getReason();
        UpdateOrderStatusResponse response = orderService.rejectOrder(userDto.getId(), orderId, storeId, reason);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [사장] 주문 상태 변경
     * - 로그인한 OWNER의 가게 주문만 상태 변경 가능
     */
    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> updateOrderStatusByStore(
            @CurrentUser UserDto userDto,
            @PathVariable("orderId") UUID orderId,
            @RequestParam("storeId") UUID storeId,
            @RequestBody @Valid UpdateOrderStatusRequest request
    ) {
        UpdateOrderStatusResponse response =
                orderService.updateOrderStatusByStore(userDto.getId(), orderId, storeId, request);
        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }
}