package com.team.project.domain.payment.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.exception.OrderNotFoundException;
import com.team.project.domain.order.repository.OrderRepository;
import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.exception.InvalidPaymentRequestException;
import com.team.project.domain.payment.exception.PaymentAlreadyPaidException;
import com.team.project.domain.payment.exception.PaymentNotFoundException;
import com.team.project.domain.payment.infrastructure.PgProviderService;
import com.team.project.domain.payment.infrastructure.dto.CancelPgProviderPaymentCommand;
import com.team.project.domain.payment.infrastructure.dto.CancelPgProviderPaymentQuery;
import com.team.project.domain.payment.infrastructure.dto.ConfirmPgProviderPaymentCommand;
import com.team.project.domain.payment.model.dto.CancelPaymentCommand;
import com.team.project.domain.payment.model.dto.CancelPaymentQuery;
import com.team.project.domain.payment.model.dto.CreatePaymentCommand;
import com.team.project.domain.payment.model.dto.CreatePaymentQuery;
import com.team.project.domain.payment.model.dto.GetPaymentQuery;
import com.team.project.domain.payment.model.dto.PayPaymentCommand;
import com.team.project.domain.payment.model.dto.PayPaymentQuery;
import com.team.project.domain.payment.model.dto.RequestCancelPaymentCommand;
import com.team.project.domain.payment.model.dto.RequestCancelPaymentQuery;
import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.domain.payment.repository.PaymentRepository;
import com.team.project.domain.payment_log.model.dto.CreatePaymentLogCommand;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;
import com.team.project.domain.payment_log.service.PaymentLogService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final PaymentLogService paymentLogService;
	private final PgProviderService pgProviderService;
	private final OrderRepository orderRepository;

	/**
	 * 결제 준비 메서드
	 */
	@Override
	@Transactional
	public CreatePaymentQuery createPayment(CreatePaymentCommand command) {
		// 1. 가장 최신 결제 확인(삭제 포함)
		Optional<Payment> latest = paymentRepository.getLatestPaymentByOrderContainsDeleted(command.getOrderId());
		// 2. 이미 결제 시도한 경우
		if (latest.isPresent()) {
			Payment payment = latest.get();
			if (payment.getStatus().isReady()) {
				return CreatePaymentQuery.from(command.getOrderId(), payment);
			} else {
				throw new PaymentAlreadyPaidException();
			}
		}

		// 2. 결제 데이터 생성
		// 2-1. 주문 찾아오기
		Order foundOrder = orderRepository.findById(command.getOrderId()).orElseThrow(OrderNotFoundException::new);
		// 2-2. 결제 생성
		Payment payment = Payment.builder()
			.status(PaymentStatus.READY)
			.amount(command.getAmount())
			.order(foundOrder)
			.build();

		// 3. 결제 데이터 저장
		Payment savedPayment = paymentRepository.createPayment(payment);

		// 4. 리턴
		return CreatePaymentQuery.from(command.getOrderId(), savedPayment);

	}

	/**
	 *  결제 성공 시에 사용되는 메서드
	 */
	@Override
	@Transactional
	public PayPaymentQuery payPayment(PayPaymentCommand command) {
		// 1. 가장 최신 결제 가져오기
		Payment payment = getLatestPaymentByOrderAndOrderStatusInnerWithException(command.getOrderId(),
			PaymentStatus.READY);

		// 2. PG사 승인 호출
		ConfirmPgProviderPaymentCommand confirmPaymentCommand = ConfirmPgProviderPaymentCommand.builder()
			.paymentKey(command.getPaymentKey())
			.amount(command.getAmount())
			.orderId(command.getOrderId().toString())
			.build();
		pgProviderService.confirmPayment(confirmPaymentCommand);

		// 3. 결제 상태 변경
		String paymentKey = command.getPaymentKey();
		payment.pay(paymentKey);

		// 4. 결제 로그 생성
		CreatePaymentLogCommand paymentLogCommand = CreatePaymentLogCommand.builder()
			.paymentKey(paymentKey)
			.status(PaymentLogStatus.SUCCESS)
			.payment(payment)
			.build();

		// 5. 결제 로그 저장
		paymentLogService.createPaymentLog(paymentLogCommand);

		return PayPaymentQuery.from(payment);
	}

	/**
	 * 결제 취소 요청하는 메서드
	 */
	@Override
	@Transactional
	public RequestCancelPaymentQuery requestCancelPayment(RequestCancelPaymentCommand command) {

		// 1. 가장 최신 결제 가져오기
		Payment payment = getLatestPaymentByOrderAndOrderStatusInnerWithException(command.getOrderId(),
			PaymentStatus.PAID);

		// 2. 결제 상태 변경
		payment.requestCancel();

		return RequestCancelPaymentQuery.from(payment);
	}

	/**
	 * 결제 취소하는 메서드
	 */
	@Override
	@Transactional
	public CancelPaymentQuery cancelPayment(CancelPaymentCommand command) {

		// 1. 가장 최신 결제 가져오기
		Payment payment = getLatestPaymentByOrderAndOrderStatusInnerWithException(command.getOrderId(),
			PaymentStatus.CANCEL_REQUESTED);

		// 2. PG사 결제 취소
		CancelPgProviderPaymentCommand pgProviderCommand = CancelPgProviderPaymentCommand.builder()
			.paymentKey(payment.getPaymentKey())
			.reason(command.getReason())
			.build();
		CancelPgProviderPaymentQuery pgProviderQuery = pgProviderService.cancelPayment(pgProviderCommand);

		// 3. 결제 상태 변경
		payment.cancel();

		return CancelPaymentQuery.from(payment);
	}

	/**
	 * 결제 데이터 삭제하는 메서드
	 */
	@Override
	@Transactional
	public void deletePayment(UUID paymentId) {

		// 1. 결제 기본키로 결제 데이터를 찾는다, 검색 결과가 없다면 예외 반환
		Payment payment = getPaymentInnerWithException(paymentId);

		// 2. 요청 중인 결제는 삭제하지 못한다.
		if (payment.getStatus().isInProgress()) {
			throw new InvalidPaymentRequestException();
		}

		// 3. 삭제 표시한다.
		payment.markDeleted(null); // 수정 필요

	}

	/**
	 * 결제 데이터 단건 조회하는 메서드
	 */
	@Override
	public GetPaymentQuery getPayment(UUID paymentId) {

		// 1. 결제 기본키로 결제 데이터를 찾는다, 검색 결과가 없다면 예외 반환
		Payment payment = getPaymentInnerWithException(paymentId);

		return GetPaymentQuery.from(payment);
	}

	/**
	 * 내부 함수들
	 */
	public Payment getPaymentInnerWithException(UUID paymentId) {
		return paymentRepository.getPayment(paymentId).orElseThrow(PaymentNotFoundException::new);
	}

	private Payment getLatestPaymentByOrderAndOrderStatusInnerWithException(UUID orderId, PaymentStatus status) {
		return paymentRepository.getLatestPaymentByOrderAndStatus(orderId, status)
			.orElseThrow(PaymentNotFoundException::new);
	}

}
