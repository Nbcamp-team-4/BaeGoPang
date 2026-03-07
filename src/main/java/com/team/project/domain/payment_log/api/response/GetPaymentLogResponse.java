package com.team.project.domain.payment_log.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.domain.payment_log.model.dto.GetPaymentLogQuery;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPaymentLogResponse {

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

	public static GetPaymentLogResponse from(GetPaymentLogQuery query) {
		return GetPaymentLogResponse.builder()
			.id(query.getId())
			.paymentKey(query.getPaymentKey())
			.status(query.getStatus())
			.paymentStatus(query.getPaymentStatus())
			.paymentAmount(query.getPaymentAmount())
			.paidAt(query.getPaidAt())
			.createdAt(query.getCreatedAt())
			.createdBy(query.getCreatedBy())
			.updatedAt(query.getUpdatedAt())
			.updatedBy(query.getUpdatedBy())
			.build();
	}
}
