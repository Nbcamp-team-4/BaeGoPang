package com.team.project.domain.payment_log.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment_log.api.request.GetPaymentLogsRequest;
import com.team.project.domain.payment_log.api.request.GetPaymentLogsResponse;
import com.team.project.domain.payment_log.api.response.GetPaymentLogResponse;
import com.team.project.domain.payment_log.entity.PaymentLog;
import com.team.project.domain.payment_log.exception.PaymentLogNotFoundException;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;
import com.team.project.domain.payment_log.repository.PaymentLogRepository;
import com.team.project.domain.pg_provider.entity.PgProvider;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentLogServiceImpl implements PaymentLogService {

	private final PaymentLogRepository paymentLogRepository;

	@Override
	public PaymentLog createPaymentLog(PaymentLog paymentLog) {
		PaymentLog saved = paymentLogRepository.createPaymentLog(paymentLog);
		return saved;
	}

	@Override
	public void deletePaymentLog(UUID paymentLogId) {

		// 1. 결제 로그 ID로 결제 로그를 조회한다.
		// 없다면 PaymentLogNotFound 에러 발생
		PaymentLog paymentLog = getPaymentLogInnerWithException(paymentLogId);

		// 2. 결제 로그 삭제
		paymentLog.markDeleted(null);

	}

	public GetPaymentLogResponse getPaymentLog(UUID paymentLogId) {
		// 1. 결제 로그 ID로 결제 로그를 조회한다.
		// 없다면 PaymentLogNotFound 에러 발생
		PaymentLog paymentLog = getPaymentLogInnerWithException(paymentLogId);
		Payment payment = paymentLog.getPayment();
		PgProvider pgProvider = paymentLog.getPgProvider();

		// PG사 결제인 경우
		if (pgProvider != null) {
			return GetPaymentLogResponse.builder()
				.id(paymentLog.getId())
				.status(paymentLog.getStatus())
				.reason(paymentLog.getReason())
				.paymentMethod(payment.getMethod())
				.paymentStatus(payment.getStatus())
				.paymentAmount(payment.getAmount())
				.pgProviderId(pgProvider.getId())
				.pgProviderName(pgProvider.getName())
				.pgProviderCode(pgProvider.getCode())
				.createdAt(paymentLog.getCreatedAt())
				.createdBy(paymentLog.getCreatedBy())
				.build();
			// 카드 결제인 경우
		} else {
			return GetPaymentLogResponse.builder()
				.id(paymentLog.getId())
				.status(paymentLog.getStatus())
				.reason(paymentLog.getReason())
				.paymentMethod(payment.getMethod())
				.paymentStatus(payment.getStatus())
				.paymentAmount(payment.getAmount())
				.createdAt(paymentLog.getCreatedAt())
				.createdBy(paymentLog.getCreatedBy())
				.build();
		}
	}

	public PaymentLog getPaymentLogInnerWithException(UUID paymentLogId) {

		// 1. 결제 로그 ID로 결제 로그를 조회한다.
		// 없다면 PaymentLogNotFound 에러 발생
		PaymentLog paymentLog = paymentLogRepository.getPaymentLog(paymentLogId)
			.orElseThrow(PaymentLogNotFoundException::new);

		return paymentLog;
	}

	@Override
	public GetPaymentLogsResponse getPaymentLogs(GetPaymentLogsRequest request) {
		// 1. 조건 파싱
		PaymentLogStatus status = request.getStatus();

		// 2. 페이징 객체 생성
		Pageable pageable = PageRequest.of(
			request.getPage(),
			request.getSize(),
			Sort.by(Sort.Direction.DESC, "createdAt")
		);

		// 3. repository 조회 (조건 기반)
		Page<PaymentLog> pageResult = paymentLogRepository.getPaymentLogs(
			status,
			request.getRangeCreatedAt(),
			pageable
		);

		// 4. Entity → DTO 변환
		List<GetPaymentLogsResponse.Item> contents = pageResult.getContent()
			.stream()
			.map((PaymentLog content) -> GetPaymentLogsResponse.Item.builder().build())
			.toList();

		// 5️⃣ 응답 생성
		return GetPaymentLogsResponse.builder()
			.content(contents)
			.page(pageResult.getNumber())
			.size(pageResult.getSize())
			.totalElements(pageResult.getTotalElements())
			.totalPages(pageResult.getTotalPages())
			.build();
	}

}


