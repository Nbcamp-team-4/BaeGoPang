package com._team._project.domain.payment.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com._team._project.domain.payment.api.request.CreatePaymentRequest;
import com._team._project.domain.payment.api.response.CancelPaymentResponse;
import com._team._project.domain.payment.api.response.CreatePaymentResponse;
import com._team._project.domain.payment.api.response.GetPaymentResponse;
import com._team._project.domain.payment.api.response.PayPaymentResponse;
import com._team._project.domain.payment.entity.Payment;
import com._team._project.domain.payment.exception.InvalidPaymentMethodException;
import com._team._project.domain.payment.exception.PaymentNotFoundException;
import com._team._project.domain.payment.model.vo.PaymentMethod;
import com._team._project.domain.payment.model.vo.PaymentStatus;
import com._team._project.domain.payment.repository.PaymentRepository;
import com._team._project.domain.payment_log.entity.PaymentLog;
import com._team._project.domain.payment_log.model.vo.PaymentLogStatus;
import com._team._project.domain.payment_log.service.PaymentLogService;
import com._team._project.domain.pg_provider.entity.PgProvider;
import com._team._project.domain.pg_provider.service.PgProviderService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final PgProviderService pgProviderService;
	private final PaymentLogService paymentLogService;

	@Override
	@Transactional
	public CreatePaymentResponse createPayment(CreatePaymentRequest request) {

		// 1. 주문 확인, 주문이 PENDING상태인 경우만 결제 데이터 생성 <- 선택(order에서 payment를 생성하는 경우, 구현할필요없음)

		// 2. 결제 데이터와 결제 로그 데이터 생성
		// 2-1. PG 결제인 경우
		if (request.getMethod() == PaymentMethod.PG_PROVIDER) {
			// 1. PG사 관련 정보 확인
			if (request.getPgProviderId() == null || request.getPgTid() == null) {
				// pg사에서 결제하는데 PG사 관련 결제 정보가 없다면 에러 발생
				throw new InvalidPaymentMethodException();
			}

			// 2. 결제 정보 생성
			Payment payment = Payment.builder()
				.method(request.getMethod())
				.status(PaymentStatus.READY)
				.amount(request.getAmount())
				.order(request.getOrderId())
				.build();

			// 3. 결제 로그 생성
			// 3-1. PgProvider 찾아오기
			PgProvider pgProvider = pgProviderService.getPgProviderInnerWithException(request.getPgProviderId());
			// 3-2. PaymentLog 생성
			PaymentLog paymentLog = PaymentLog.builder()
				.tid(request.getPgTid())
				.status(PaymentLogStatus.READY)
				.payment(payment)
				.pgProvider(pgProvider)
				.build();

			// 4. 결제 로그 저장
			PaymentLog savedPaymentLog = paymentLogService.createPaymentLog(paymentLog);

			// 5. 결제 정보 저장
			Payment savedPayment = paymentRepository.createPayment(payment);

			// 6. 리턴
			return CreatePaymentResponse.builder()
				.id(savedPayment.getId())
				.status(savedPayment.getStatus())
				.method(savedPayment.getMethod())
				.amount(savedPayment.getAmount())
				.orderNo("test_number")
				.orderStatus("test_status")
				.pgCode(pgProvider.getCode())
				.pgName(pgProvider.getName())
				.createdAt(savedPayment.getCreatedAt())
				.createdBy(savedPayment.getCreatedBy())
				.build();
			// 2-2. 카드 결제인 경우
		} else {
			// 1. 결제 정보 생성
			Payment payment = Payment.builder()
				.method(request.getMethod())
				.status(PaymentStatus.READY)
				.amount(request.getAmount())
				.order(request.getOrderId())
				.build();

			// 2. 결제 로그 생성
			// 2-1. PaymentLog 생성
			PaymentLog paymentLog = PaymentLog.builder()
				.status(PaymentLogStatus.READY)
				.payment(payment)
				.build();

			// 3. 결제 로그 저장
			PaymentLog savedPaymentLog = paymentLogService.createPaymentLog(paymentLog);

			// 4. 결제 저장
			Payment saved = paymentRepository.createPayment(payment);
			// 5. 리턴
			return CreatePaymentResponse.builder()
				.id(saved.getId())
				.status(saved.getStatus())
				.method(saved.getMethod())
				.amount(saved.getAmount())
				.orderNo("test_number")
				.orderStatus("test_status")
				.createdAt(saved.getCreatedAt())
				.createdBy(saved.getCreatedBy())
				.build();
		}
	}

	@Override
	@Transactional
	public PayPaymentResponse payPayment(UUID paymentId) {

		Payment payment = paymentRepository.getPayment(paymentId).orElseThrow(PaymentNotFoundException::new);

		payment.pay();

		return PayPaymentResponse.from(payment);
	}

	@Override
	@Transactional
	public CancelPaymentResponse cancelPayment(UUID paymentId) {

		Payment payment = paymentRepository.getPayment(paymentId).orElseThrow(PaymentNotFoundException::new);

		payment.cancel();

		// 로그 생성

		return CancelPaymentResponse.from(payment);
	}

	@Override
	@Transactional
	public void deletePayment(UUID paymentId) {

		// 1. 결제 기본키로 결제 데이터를 찾는다, 검색 결과가 없다면 예외 반환
		Payment payment = paymentRepository.getPayment(paymentId).orElseThrow(PaymentNotFoundException::new);

		// 2. 삭제 표시한다.
		payment.markDeleted(null); // 수정 필요

	}

	@Override
	public GetPaymentResponse getPayment(UUID paymentId) {

		// 1. 결제 기본키로 결제 데이터를 찾는다, 검색 결과가 없다면 예외 반환
		Payment payment = paymentRepository.getPayment(paymentId).orElseThrow(PaymentNotFoundException::new);

		return GetPaymentResponse.from(payment);
	}

}
