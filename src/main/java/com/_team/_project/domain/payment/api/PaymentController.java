package com._team._project.domain.payment.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._team._project.domain.payment.api.request.CreatePaymentRequest;
import com._team._project.domain.payment.api.response.CreatePaymentResponse;
import com._team._project.domain.payment.service.PaymentService;
import com._team._project.global.common.dto.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping
	public ResponseEntity<?> createPayment(@RequestBody @Valid CreatePaymentRequest request) {

		CreatePaymentResponse response = paymentService.createPayment(request);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(
				response
			)
		);
	}
}
