package com.team.project.domain.payment.infrastructure.gateway.toss;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.team.project.domain.payment.infrastructure.PgProviderService;
import com.team.project.domain.payment.infrastructure.dto.CancelPgProviderPaymentCommand;
import com.team.project.domain.payment.infrastructure.dto.CancelPgProviderPaymentQuery;
import com.team.project.domain.payment.infrastructure.dto.ConfirmPgProviderPaymentCommand;
import com.team.project.domain.payment.infrastructure.dto.ConfirmPgProviderPaymentQuery;
import com.team.project.domain.payment.infrastructure.gateway.toss.dto.TossCancelResponse;
import com.team.project.domain.payment.infrastructure.gateway.toss.dto.TossConfirmResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 토스 연동에서 업무 의미를 처리
 */
@Service
@Profile("toss")
@RequiredArgsConstructor
@Slf4j
public class TossPaymentService implements PgProviderService {

	private final TossPaymentClient tossPaymentClient;

	/**
	 *  결제 승인
	 */
	@Override
	public ConfirmPgProviderPaymentQuery confirmPayment(ConfirmPgProviderPaymentCommand command) {

		// 1. 토스 결제 승인 호출
		TossConfirmResponse tossReponse = tossPaymentClient.confirm(command.getPaymentKey(),
				command.getOrderId().toString(), command.getAmount());

		// 2. DTO로 변환
		ConfirmPgProviderPaymentQuery response = ConfirmPgProviderPaymentQuery.builder()
				.paymentKey(tossReponse.getPaymentKey())
				.orderId(UUID.fromString(tossReponse.getOrderId()))
				.amount(tossReponse.getTotalAmount())
				.build();

		return response;
	}

	/**
	 *  결제 취소
	 */
	@Override
	public CancelPgProviderPaymentQuery cancelPayment(CancelPgProviderPaymentCommand command) {

		// 1. 토스 결제 취소 호출
		TossCancelResponse tossResponse = tossPaymentClient.cancel(command.getPaymentKey(), command.getReason());

		// 2. DTO로 변환
		if (tossResponse.getCancels() == null || tossResponse.getCancels().isEmpty()) {
			return CancelPgProviderPaymentQuery.builder()
					.paymentKey(tossResponse.getPaymentKey())
					.orderId(UUID.fromString(tossResponse.getOrderId()))
					.status(tossResponse.getStatus())
					.build();
		}
		TossCancelResponse.Cancel cancel = tossResponse.getCancels().get(0);

		CancelPgProviderPaymentQuery response = CancelPgProviderPaymentQuery.builder()
				.paymentKey(tossResponse.getPaymentKey())
				.orderId(UUID.fromString(tossResponse.getOrderId()))
				.status(tossResponse.getStatus())
				.cancelReason(cancel.getCancelReason())
				.cancelAmount(cancel.getCancelAmount())
				.canceledAt(cancel.getCanceledAt())
				.build();

		return response;
	}

}
