package com.team.project.domain.payment_log.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.domain.payment_log.entity.PaymentLog;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPaymentLogQuery {

	private UUID id;
	private String paymentKey;
	private PaymentLogStatus status;
	private String reason;
	private PaymentStatus paymentStatus;
	private Integer paymentAmount;
	private LocalDateTime paidAt;
	private LocalDateTime createdAt;
	private UUID createdBy;
	private LocalDateTime updatedAt;
	private UUID updatedBy;

	public static GetPaymentLogQuery from(PaymentLog paymentLog) {
		return GetPaymentLogQuery.builder()
			.id(paymentLog.getId())
			.paymentKey(paymentLog.getPaymentKey())
			.status(paymentLog.getStatus())
			.paymentStatus(paymentLog.getPayment().getStatus())
			.paymentAmount(paymentLog.getPayment().getAmount())
			.paidAt(paymentLog.getPayment().getPaidAt())
			.createdAt(paymentLog.getCreatedAt())
			.createdBy(paymentLog.getCreatedBy())
			.updatedAt(paymentLog.getUpdatedAt())
			.updatedBy(paymentLog.getUpdatedBy())
			.build();
	}
}
