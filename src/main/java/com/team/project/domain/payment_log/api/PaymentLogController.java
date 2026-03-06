package com.team.project.domain.payment_log.api;

import java.util.UUID;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payment-logs")
@Slf4j
@RequiredArgsConstructor
public class PaymentLogController {

	private final PaymentLogService paymentLogService;

	/**
	 * 결제 로그 데이터 단건 조회 api
	 */
	@GetMapping("/{paymentLogId}")
	public ResponseEntity<?> getPaymentLog(@PathVariable("paymentLogId") UUID paymentLogId) {

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
	@GetMapping
	public ResponseEntity<?> getPaymentLogs(@ModelAttribute GetPaymentLogsRequest request) {

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
