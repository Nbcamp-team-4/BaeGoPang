package com.team.project.domain.payment.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.order.entity.Order;
import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.exception.InvalidPaymentRequestException;
import com.team.project.domain.payment.exception.PaymentAlreadyPaidException;
import com.team.project.domain.payment.exception.PaymentAmountMismatchException;
import com.team.project.domain.payment.exception.PaymentForbiddenException;
import com.team.project.domain.payment.exception.PaymentNotFoundException;
import com.team.project.domain.payment.infrastructure.PgProviderService;
import com.team.project.domain.payment.infrastructure.dto.CancelPgProviderPaymentCommand;
import com.team.project.domain.payment.infrastructure.dto.CancelPgProviderPaymentQuery;
import com.team.project.domain.payment.infrastructure.dto.ConfirmPgProviderPaymentCommand;
import com.team.project.domain.payment.infrastructure.dto.ConfirmPgProviderPaymentQuery;
import com.team.project.domain.payment.infrastructure.exception.PgProviderBaseException;
import com.team.project.domain.payment.model.dto.CancelPaymentCommand;
import com.team.project.domain.payment.model.dto.CancelPaymentQuery;
import com.team.project.domain.payment.model.dto.CreatePaymentCommand;
import com.team.project.domain.payment.model.dto.CreatePaymentQuery;
import com.team.project.domain.payment.model.dto.GetPaymentQuery;
import com.team.project.domain.payment.model.dto.GetPaymentsCommand;
import com.team.project.domain.payment.model.dto.GetPaymentsQuery;
import com.team.project.domain.payment.model.dto.PayPaymentCommand;
import com.team.project.domain.payment.model.dto.PayPaymentQuery;
import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.domain.payment.repository.PaymentRepository;
import com.team.project.domain.payment_log.model.dto.CreatePaymentLogCommand;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;
import com.team.project.domain.payment_log.service.PaymentLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final PaymentLogService paymentLogService;
	private final PgProviderService pgProviderService;

	/**
	 * 결제 준비 메서드
	 */
	@Override
	@Transactional
	public CreatePaymentQuery createPayment(CreatePaymentCommand command) {

		Order order = command.getOrder();
		// 1. 가장 최신 결제 확인(삭제 포함)
		Optional<Payment> latest = paymentRepository.getLatestPaymentByOrderContainsDeleted(order.getId());
		// 2. 이미 결제 시도한 경우
		if (latest.isPresent()) {
			Payment payment = latest.get();
			if (payment.getStatus().isReady()) {
				return CreatePaymentQuery.from(order.getId(), payment);
			} else {
				throw new PaymentAlreadyPaidException();
			}
		}

		// 2. 결제 데이터 생성
		// 2-1. 주문 찾아오기
		// 2-2. 결제 생성
		Payment payment = Payment.builder()
			.status(PaymentStatus.READY)
			.amount(command.getAmount())
			.order(order)
			.build();

		// 3. 결제 데이터 저장
		Payment savedPayment = paymentRepository.createPayment(payment);

		// 4. 리턴
		return CreatePaymentQuery.from(order.getId(), savedPayment);

	}

	/**
	 * 결제 성공 시에 사용되는 메서드
	 */
	@Override
	@Transactional
	public PayPaymentQuery payPayment(PayPaymentCommand command) {
		// 1. 가장 최신 결제 가져오기
		Payment payment = getLatestPaymentByOrderAndOrderStatusInnerWithException(command.getOrderId(),
			PaymentStatus.READY);

		// 2. 검증
		if (!payment.getAmount().equals(command.getAmount())) {
			paymentLogService.createPaymentFailureLog(
				CreatePaymentLogCommand.of(
					command.getPaymentKey(),
					PaymentLogStatus.PAY_FAILURE,
					"결제 금액 불일치",
					payment.getId()
				)
			);
			throw new PaymentAmountMismatchException();
		}

		// 3. PG사 승인 호출
		try {

			ConfirmPgProviderPaymentQuery pgProviderQuery = pgProviderService.confirmPayment(
				ConfirmPgProviderPaymentCommand.of(command.getPaymentKey(), command.getOrderId().toString(),
					payment.getAmount()));

			// 4-1. 결제 상태 변경
			String paymentKey = command.getPaymentKey();
			payment.pay(paymentKey);

			// 5. PG사 통신 성공 경우 로그 생성
			paymentLogService.createPaymentLog(
				CreatePaymentLogCommand.of(paymentKey, PaymentLogStatus.PAY_SUCCESS,
					null, payment.getId()));

			return PayPaymentQuery.from(payment);
		} catch (PgProviderBaseException e) {
			// 4-2. PG사 통신 실패 경우 로그 생성
			paymentLogService.createPaymentFailureLog(
				CreatePaymentLogCommand.of(command.getPaymentKey(), PaymentLogStatus.PAY_FAILURE,
					e.getMessage(), payment.getId())
			);

			throw e;
		}catch(Payment)
	}

	/**
	 * 결제 취소하는 메서드
	 */
	@Override
	@Transactional
	public CancelPaymentQuery cancelPayment(CancelPaymentCommand command) {

		// 1. 가장 최신 결제 가져오기
		Payment payment = getLatestPaymentByOrderAndOrderStatusInnerWithException(command.getOrderId(),
			PaymentStatus.PAID);

		// 2. 검증
		String type = command.getType();
		if (!"REFUND".equals(type) && !"CANCEL".equals(type)) {
			throw new InvalidPaymentRequestException();
		}

		// 2. PG사 결제 취소
		try {
			CancelPgProviderPaymentQuery pgProviderQuery = pgProviderService.cancelPayment(
				CancelPgProviderPaymentCommand.of(payment.getPaymentKey(),
					command.getReason()));

			// 3. 결제 상태 변경
			payment.cancel();

			// 2-2. PG 통신 성공 경우 로그 생성
			String paymentKey = "test_paymentKey";
			paymentLogService.createPaymentLog(
				CreatePaymentLogCommand.of(paymentKey, PaymentLogStatus.valueOf(type + "_SUCCESS"),
					command.getReason(), payment.getId()));

		} catch (PgProviderBaseException e) {
			// 2-1. PG 통신 실패 경우 로그 생성
			paymentLogService.createPaymentFailureLog(
				CreatePaymentLogCommand.of(payment.getPaymentKey(), PaymentLogStatus.valueOf(type + "_FAILURE"),
					command.getReason(), payment.getId())
			);

			// 2-2. 결제 취소 실패
			payment.cancelFailed();
		}

		return CancelPaymentQuery.from(payment);
	}

	/**
	 * 결제 데이터 삭제하는 메서드
	 */
	@Override
	@Transactional
	public void deletePayment(UUID paymentId, UserDto userDto) {

		// 1. 결제 기본키로 결제 데이터를 찾는다, 검색 결과가 없다면 예외 반환
		Payment payment = getPaymentInnerWithException(paymentId);

		// 2. 요청 중인 결제는 삭제하지 못한다.
		if (payment.getStatus().isInProgress()) {
			throw new InvalidPaymentRequestException();
		}

		// 3. 삭제 권한을 확인한다.
		if (!payment.getCreatedBy().equals(userDto.getLoginId())) {
			throw new PaymentForbiddenException();
		}

		// 4. 삭제 표시한다.
		payment.markDeleted(userDto.getId());

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
	 * 결제 데이터 조회하는 메서드
	 */
	@Override
	public GetPaymentsQuery getPayments(GetPaymentsCommand command) {

		// 1. 페이징 객체 생성
		Pageable pageable = PageRequest.of(
			command.getPage(),
			command.getSize(),
			Sort.by(Sort.Direction.DESC, "createdAt")
		);

		// 2. 여러 조건을 기준으로 결제를 조회
		Page<Payment> pageResult = paymentRepository.getPayments(
			command.getPaymentStatus(),
			command.getRangeAmount(),
			command.getRangePaidAt(),
			command.getOrderId(),
			pageable
		);

		return GetPaymentsQuery.from(pageResult);
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
