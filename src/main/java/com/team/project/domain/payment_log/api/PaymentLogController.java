package com.team.project.domain.payment_log.api;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.payment_log.api.request.GetPaymentLogsRequest;
import com.team.project.domain.payment_log.api.response.GetPaymentLogResponse;
import com.team.project.domain.payment_log.api.response.GetPaymentLogsResponse;
import com.team.project.domain.payment_log.model.dto.GetPaymentLogQuery;
import com.team.project.domain.payment_log.model.dto.GetPaymentLogsCommand;
import com.team.project.domain.payment_log.model.dto.GetPaymentLogsQuery;
import com.team.project.domain.payment_log.service.PaymentLogService;
import com.team.project.global.common.dto.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "PaymentLog", description = "결제 로그 API")
@RestController
@RequestMapping(value = "/api/payment-logs", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class PaymentLogController {

	private final PaymentLogService paymentLogService;

	/**
	 * 결제 로그 데이터 단건 조회 api
	 */
	@Operation(summary = "결제 로그 단건 조회", description = "paymentLogId로 결제 로그 데이터를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "결제 로그 조회 성공"),
		@ApiResponse(responseCode = "404", description = "결제 로그를 찾을 수 없음", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = BaseResponse.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					{
					  "success": false,
					  "data": null,
					  "errorCode": "PAYMENT_LOG_NOT_FOUND"
					}
					"""
			)
		))
	})
	@GetMapping("/{paymentLogId}")
	public ResponseEntity<BaseResponse<GetPaymentLogResponse>> getPaymentLog(
		@PathVariable("paymentLogId") UUID paymentLogId) {

		// 1. service 호출
		GetPaymentLogQuery query = paymentLogService.getPaymentLog(paymentLogId);

		// 2. dto변환
		GetPaymentLogResponse response = GetPaymentLogResponse.from(query);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(response)
		);
	}

	/**
	 * 결제 로그 데이터 전체 조회 api
	 */
	@Operation(summary = "결제 로그 목록 조회", description = "조건에 따라 결제 로그 데이터를 페이지 단위로 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "결제 로그 목록 조회 성공",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = GetPaymentLogsResponse.class)
			)
		)
	})
	@GetMapping
	public ResponseEntity<BaseResponse<GetPaymentLogsResponse>> getPaymentLogs(
		@ParameterObject @ModelAttribute GetPaymentLogsRequest request) {

		// 1. service dto 변환
		GetPaymentLogsCommand command = GetPaymentLogsCommand.of(request.getPage(), request.getSize(),
			request.getStatus(), request.getRangeCreatedAt());

		// 2. service 호출
		GetPaymentLogsQuery query = paymentLogService.getPaymentLogs(command);

		// 3. dto 변환
		GetPaymentLogsResponse response = GetPaymentLogsResponse.from(query);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(response)
		);

	}

}
