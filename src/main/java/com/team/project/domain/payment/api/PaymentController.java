package com.team.project.domain.payment.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	/**
	 * 결제 데이터를 단건조회하는 api
	 */
	@GetMapping("/{paymentId}")
	public ResponseEntity<?> getPayment(@PathVariable("paymentId") UUID paymentId) {

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
	@GetMapping
	public ResponseEntity<?> getPayments(GetPaymentsRequest request) {

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
	@DeleteMapping("/{paymentId}")
	public ResponseEntity<?> deletePayment(@PathVariable("paymentId") UUID paymentId) {

		// 1. service 호출
		paymentService.deletePayment(paymentId);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(
				null
			)
		);
	}

}
