package com._team._project.domain.payment_log.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._team._project.domain.payment_log.api.request.GetPaymentLogsRequest;
import com._team._project.domain.payment_log.api.request.GetPaymentLogsResponse;
import com._team._project.domain.payment_log.api.response.GetPaymentLogResponse;
import com._team._project.domain.payment_log.service.PaymentLogService;
import com._team._project.global.common.dto.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payment-logs")
@Slf4j
@RequiredArgsConstructor
public class PaymentLogController {

	private final PaymentLogService paymentLogService;

	@GetMapping("/{paymentLogId}")
	public ResponseEntity<?> getPaymentLog(@PathVariable("paymentLogId") UUID paymentLogId) {

		GetPaymentLogResponse response = paymentLogService.getPaymentLog(paymentLogId);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(response)
		);
	}

	@GetMapping
	public ResponseEntity<?> getPaymentLogs(@ModelAttribute GetPaymentLogsRequest request) {

		GetPaymentLogsResponse response = paymentLogService.getPaymentLogs(request);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(response)
		);

	}

}
