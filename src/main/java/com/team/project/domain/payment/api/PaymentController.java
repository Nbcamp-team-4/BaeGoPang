package com.team.project.domain.payment.api;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.payment.api.request.GetPaymentsRequest;
import com.team.project.domain.payment.api.response.GetPaymentResponse;
import com.team.project.domain.payment.api.response.GetPaymentsResponse;
import com.team.project.domain.payment.model.dto.GetPaymentQuery;
import com.team.project.domain.payment.model.dto.GetPaymentsCommand;
import com.team.project.domain.payment.model.dto.GetPaymentsQuery;
import com.team.project.domain.payment.service.PaymentService;
import com.team.project.global.common.dto.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Payment", description = "결제 API")
@RestController
@RequestMapping(value = "/api/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	/**
	 * 결제 데이터를 단건조회하는 api
	 */
	@Operation(summary = "결제 단건 조회", description = "paymentId로 결제 데이터를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "결제 조회 성공"),
		@ApiResponse(responseCode = "404", description = "결제를 찾을 수 없음", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = BaseResponse.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					{
					  "success": false,
					  "data": null,
					  "errorCode": "PAYMENT_NOT_FOUND"
					}
					"""
			)
		))
	})
	@GetMapping("/{paymentId}")
	public ResponseEntity<BaseResponse<GetPaymentResponse>> getPayment(
		@Parameter(description = "결제 ID", required = true) @PathVariable("paymentId") UUID paymentId) {

		// 1. service 호출
		GetPaymentQuery query = paymentService.getPayment(paymentId);

		// 2. dto 변환
		GetPaymentResponse response = GetPaymentResponse.from(query);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(
				response
			)
		);
	}

	/**
	 * 결제 데이터를 전체 조회하는 api
	 */
	@Operation(summary = "결제 목록 조회", description = "조건에 따라 결제 데이터를 페이지 단위로 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "결제 목록 조회 성공")
	})
	@GetMapping
	public ResponseEntity<?> getPayments(@ParameterObject @ModelAttribute GetPaymentsRequest request) {

		// 1. service dto 변환
		GetPaymentsCommand command = GetPaymentsCommand.of(request.getPage(), request.getSize(),
			request.getPaymentStatus(), request.getRangeAmount(), request.getRangePaidAt(), request.getOrderId());

		// 2. service 호출
		GetPaymentsQuery query = paymentService.getPayments(command);

		// 3. dto 변환
		GetPaymentsResponse response = GetPaymentsResponse.from(query);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(response)
		);
	}

	/**
	 * 결제 데이터를 삭제하는 api
	 */
	@Operation(summary = "결제 삭제", description = "paymentId에 해당하는 결제 데이터를 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "결제 삭제 성공"),
		@ApiResponse(responseCode = "404", description = "결제를 찾을 수 없음", content = @Content)
	})
	@DeleteMapping("/{paymentId}")
	public ResponseEntity<?> deletePayment(
		@Parameter(description = "결제 ID", required = true) @PathVariable("paymentId") UUID paymentId) {

		// 1. service 호출
		paymentService.deletePayment(paymentId);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(
				null
			)
		);
	}

}
