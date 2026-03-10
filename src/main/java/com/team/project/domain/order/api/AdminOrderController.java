package com.team.project.domain.order.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.order.api.request.CancelOrderRequest;
import com.team.project.domain.order.api.request.UpdateOrderStatusRequest;
import com.team.project.domain.order.api.response.CancelOrderResponse;
import com.team.project.domain.order.api.response.GetOrderDetailResponse;
import com.team.project.domain.order.api.response.GetOrderSummaryResponse;
import com.team.project.domain.order.api.response.UpdateOrderStatusResponse;
import com.team.project.domain.order.service.OrderAdminService;
import com.team.project.global.common.dto.BaseResponse;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.team.project.domain.order.api.request.AdminGetOrdersRequest;
import com.team.project.domain.order.api.response.GetOrdersResponse;
import com.team.project.domain.order.model.dto.GetOrdersCommand;
import com.team.project.domain.order.model.dto.GetOrdersQuery;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "AdminOrder", description = "관리자 주문 API")
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
    @Operation(summary = "관리자 주문 목록 조회", description = "조건에 따라 주문 목록을 페이지 단위로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주문 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetOrdersResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<BaseResponse<GetOrdersResponse>> getAllOrders(
            @ParameterObject @ModelAttribute AdminGetOrdersRequest request
    ) {
        GetOrdersCommand command = GetOrdersCommand.of(
                request.getPage(),
                request.getSize(),
                request.getStatus(),
                request.getRangeCreatedAt(),
                request.getStoreId(),
                request.getUserId()
        );

        GetOrdersQuery query = orderAdminService.getAllOrders(command);
        GetOrdersResponse response = GetOrdersResponse.from(query);

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