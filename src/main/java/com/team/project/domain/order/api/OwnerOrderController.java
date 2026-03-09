package com.team.project.domain.order.api;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.auth.dto.CurrentUser;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.order.api.request.CancelOrderRequest;
import com.team.project.domain.order.api.request.OwnerGetOrdersRequest;
import com.team.project.domain.order.api.request.UpdateOrderStatusRequest;
import com.team.project.domain.order.api.response.GetOrderDetailResponse;
import com.team.project.domain.order.api.response.GetOrdersResponse;
import com.team.project.domain.order.api.response.UpdateOrderStatusResponse;
import com.team.project.domain.order.model.dto.GetOrdersCommand;
import com.team.project.domain.order.model.dto.GetOrdersQuery;
import com.team.project.domain.order.service.OrderService;
import com.team.project.global.common.dto.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "OwnerOrder", description = "사장 주문 API")
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
    @Operation(summary = "가게 주문 목록 조회", description = "조건에 따라 가게 주문 목록을 페이지 단위로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "가게 주문 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetOrdersResponse.class)
                    )
            )
    })
    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<BaseResponse<GetOrdersResponse>> getStoreOrders(
            @CurrentUser UserDto userDto,
            @RequestParam("storeId") UUID storeId,
            @ParameterObject @ModelAttribute OwnerGetOrdersRequest request
    ) {
        GetOrdersCommand command = GetOrdersCommand.of(
                request.getPage(),
                request.getSize(),
                request.getStatus(),
                request.getRangeCreatedAt(),
                storeId,
                null
        );

        GetOrdersQuery query = orderService.getStoreOrders(userDto.getId(), storeId, command);
        GetOrdersResponse response = GetOrdersResponse.from(query);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(response));
    }

    /**
     * [사장] 가게 주문 상세 조회
     * - 로그인한 OWNER의 가게인지 검증 후 조회
     */
    @Operation(summary = "가게 주문 상세 조회", description = "사장 본인 가게의 주문 상세를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 주문 상세 조회 성공")
    })
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<BaseResponse<GetOrderDetailResponse>> getStoreOrderDetail(
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
    @Operation(summary = "주문 수락", description = "사장 본인 가게의 주문을 수락합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 수락 성공")
    })
    @PutMapping("/{orderId}/accept")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<BaseResponse<UpdateOrderStatusResponse>> acceptOrder(
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
    @Operation(summary = "주문 거절", description = "사장 본인 가게의 주문을 거절합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 거절 성공")
    })
    @PutMapping("/{orderId}/reject")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<BaseResponse<UpdateOrderStatusResponse>> rejectOrder(
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
    @Operation(summary = "주문 상태 변경", description = "사장 본인 가게의 주문 상태를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 상태 변경 성공")
    })
    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<BaseResponse<UpdateOrderStatusResponse>> updateOrderStatusByStore(
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