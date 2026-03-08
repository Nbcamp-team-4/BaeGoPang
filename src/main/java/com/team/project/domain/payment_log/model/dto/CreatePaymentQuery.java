package com.team.project.domain.payment_log.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment_log.entity.PaymentLog;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreatePaymentQuery {
	private UUID id;
	private String paymentKey;
	private PaymentLogStatus status;
	private String reason;
	private UUID paymentId;
	private LocalDateTime createAt;
	private UUID createBy;

	public static CreatePaymentQuery from(PaymentLog paymentLog) {
		return CreatePaymentQuery.builder()
			.id(paymentLog.getId())
			.paymentKey(paymentLog.getPaymentKey())
			.status(paymentLog.getStatus())
			.reason(paymentLog.getReason())
			.paymentId(paymentLog.getId())
			.createAt(paymentLog.getCreatedAt())
			.createBy(paymentLog.getCreatedBy())
			.build();
	}
}
