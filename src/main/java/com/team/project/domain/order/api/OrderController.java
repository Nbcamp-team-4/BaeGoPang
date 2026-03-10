package com.team.project.domain.order.api;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.team.project.domain.order.api.request.GetOrdersRequest;
import com.team.project.domain.order.api.response.CancelOrderResponse;
import com.team.project.domain.order.api.response.ConfirmOrderPaymentResponse;
import com.team.project.domain.order.api.response.CreateOrderResponse;
import com.team.project.domain.order.api.response.GetOrderDetailResponse;
import com.team.project.domain.order.api.response.GetOrdersResponse;
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

@Tag(name = "Order", description = "주문 API")
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
	 * - 로그인한 사용자 본인 주문만 결제 가능/payment/success
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
	@Operation(summary = "내 주문 목록 조회", description = "조건에 따라 내 주문 목록을 페이지 단위로 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "내 주문 목록 조회 성공",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = GetOrdersResponse.class)
			)
		)
	})
	@GetMapping
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<BaseResponse<GetOrdersResponse>> getMyOrders(
		@CurrentUser UserDto userDto,
		@ParameterObject @ModelAttribute GetOrdersRequest request
	) {
		GetOrdersCommand command = GetOrdersCommand.of(
			request.getPage(),
			request.getSize(),
			request.getStatus(),
			request.getRangeCreatedAt(),
			null,
			null
		);

		GetOrdersQuery query = orderService.getMyOrders(userDto.getId(), command);
		GetOrdersResponse response = GetOrdersResponse.from(query);

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