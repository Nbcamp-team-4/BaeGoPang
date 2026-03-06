package com.team.project.domain.payment.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.payment.api.request.CancelPaymentRequest;
import com.team.project.domain.payment.api.request.CreatePaymentRequest;
import com.team.project.domain.payment.api.request.PayPaymentRequest;
import com.team.project.domain.payment.api.request.RequestCancelPaymentRequest;
import com.team.project.domain.payment.api.response.CancelPaymentResponse;
import com.team.project.domain.payment.api.response.CreatePaymentResponse;
import com.team.project.domain.payment.api.response.GetPaymentResponse;
import com.team.project.domain.payment.api.response.PayPaymentResponse;
import com.team.project.domain.payment.api.response.RequestCancelPaymentResponse;
import com.team.project.domain.payment.model.dto.CancelPaymentCommand;
import com.team.project.domain.payment.model.dto.CancelPaymentQuery;
import com.team.project.domain.payment.model.dto.CreatePaymentCommand;
import com.team.project.domain.payment.model.dto.CreatePaymentQuery;
import com.team.project.domain.payment.model.dto.GetPaymentQuery;
import com.team.project.domain.payment.model.dto.PayPaymentCommand;
import com.team.project.domain.payment.model.dto.PayPaymentQuery;
import com.team.project.domain.payment.model.dto.RequestCancelPaymentCommand;
import com.team.project.domain.payment.model.dto.RequestCancelPaymentQuery;
import com.team.project.domain.payment.service.PaymentService;
import com.team.project.global.common.dto.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	/**
	 * 결제 준비 api
	 */
	@PostMapping
	public ResponseEntity<?> createPayment(@RequestBody @Valid CreatePaymentRequest request) {

		// 1. service dto 변환
		CreatePaymentCommand command = CreatePaymentCommand.builder()
			.orderId(request.getOrderId())
			.amount(request.getAmount())
			.build();

		// 2. service 호출
		CreatePaymentQuery query = paymentService.createPayment(command);

		// 3. dto 변환
		CreatePaymentResponse response = CreatePaymentResponse.from(query);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(
				response
			)
		);
	}

	/**
	 * 결제 하는 api
	 */
	@PostMapping("/confirm")
	public ResponseEntity<?> confirmPayment(@RequestBody @Valid PayPaymentRequest request) {

		// 1. service dto 변환
		PayPaymentCommand command = PayPaymentCommand.builder()
			.orderId(request.getOrderId())
			.paymentKey(request.getPaymentKey())
			.amount(request.getAmount())
			.build();

		// 2. service 호출
		PayPaymentQuery query = paymentService.payPayment(command);

		// 3. dto 변환
		PayPaymentResponse response = PayPaymentResponse.from(query);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(
				response
			)
		);
	}

	/**
	 * 결제 취소 요청을 하는 api
	 */
	@PostMapping("/cancel/request")
	public ResponseEntity<?> requestCancelPayment(@RequestBody @Valid RequestCancelPaymentRequest request) {

		// 1. service dto 변환
		RequestCancelPaymentCommand command = RequestCancelPaymentCommand.builder()
			.orderId(request.getOrderId())
			.reason(request.getReason())
			.build();

		// 2. service 호출
		RequestCancelPaymentQuery query = paymentService.requestCancelPayment(command);

		// 3. dto 변환
		RequestCancelPaymentResponse response = RequestCancelPaymentResponse.from(query);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(
				response
			)
		);
	}

	/**
	 * 결제 취소하는 api
	 */
	@PostMapping("/cancel")
	public ResponseEntity<?> cancelPayment(@RequestBody @Valid CancelPaymentRequest request) {

		// 1. service dto 변환
		CancelPaymentCommand command = CancelPaymentCommand.builder()
			.orderId(request.getOrderId())
			.reason(request.getReason())
			.build();

		// 2. service 호출
		CancelPaymentQuery query = paymentService.cancelPayment(command);

		// 3. dto 변환
		CancelPaymentResponse response = CancelPaymentResponse.from(query);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(response)
		);
	}

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
