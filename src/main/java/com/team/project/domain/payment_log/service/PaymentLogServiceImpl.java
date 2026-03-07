package com.team.project.domain.payment_log.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.exception.PaymentNotFoundException;
import com.team.project.domain.payment.repository.PaymentRepository;
import com.team.project.domain.payment_log.entity.PaymentLog;
import com.team.project.domain.payment_log.exception.PaymentLogNotFoundException;
import com.team.project.domain.payment_log.model.dto.CreatePaymentLogCommand;
import com.team.project.domain.payment_log.model.dto.CreatePaymentQuery;
import com.team.project.domain.payment_log.model.dto.GetPaymentLogQuery;
import com.team.project.domain.payment_log.model.dto.GetPaymentLogsCommand;
import com.team.project.domain.payment_log.model.dto.GetPaymentLogsQuery;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;
import com.team.project.domain.payment_log.repository.PaymentLogRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentLogServiceImpl implements PaymentLogService {

	private final PaymentRepository paymentRepository;
	private final PaymentLogRepository paymentLogRepository;

	/**
	 * 결제 로그 생성 메서드
	 */
	@Transactional
	@Override
	public CreatePaymentQuery createPaymentLog(CreatePaymentLogCommand command) {
		// 1. 결제 조회
		Payment payment = paymentRepository.getPayment(command.getPaymentId())
			.orElseThrow(PaymentNotFoundException::new);

		// 2. 결제 로그 생성
		PaymentLog paymentLog = PaymentLog.builder()
			.paymentKey(command.getPaymentKey())
			.status(command.getStatus())
			.reason(command.getReason())
			.payment(payment)
			.build();

		// 2. 결제 로그 저장
		PaymentLog saved = paymentLogRepository.createPaymentLog(paymentLog);

		return CreatePaymentQuery.from(saved);
	}

	/**
	 * 결제 로그 데이터 단건 조회하는 메서드
	 */
	public GetPaymentLogQuery getPaymentLog(UUID paymentLogId) {
		// 1. 결제 로그 ID로 결제 로그를 조회
		// 없다면 PaymentLogNotFound 에러 발생
		PaymentLog paymentLog = getPaymentLogInnerWithException(paymentLogId);

		return GetPaymentLogQuery.from(paymentLog);
	}

	/**
	 * 결제 로그 데이터 전체 조회하는 메서드
	 */
	@Override
	public GetPaymentLogsQuery getPaymentLogs(GetPaymentLogsCommand command) {

		// 1. 결제 로그 상태 가져오기
		PaymentLogStatus status = command.getStatus();

		// 2. 페이징 객체 생성
		Pageable pageable = PageRequest.of(
			command.getPage(),
			command.getSize(),
			Sort.by(Sort.Direction.DESC, "createdAt")
		);

		// 3. 결제 로그 상태와 결제 로그 생성 기간을 기준으로 결제 로그를 조회
		Page<PaymentLog> pageResult = paymentLogRepository.getPaymentLogs(
			status,
			command.getRangeCreatedAt(),
			pageable
		);

		return GetPaymentLogsQuery.from(pageResult);
	}

	/**
	 * 내부 함수들
	 */
	public PaymentLog getPaymentLogInnerWithException(UUID paymentLogId) {

		// 1. 결제 로그 ID로 결제 로그를 조회한다.
		// 없다면 PaymentLogNotFound 에러 발생
		PaymentLog paymentLog = paymentLogRepository.getPaymentLog(paymentLogId)
			.orElseThrow(PaymentLogNotFoundException::new);

		return paymentLog;
	}

}


