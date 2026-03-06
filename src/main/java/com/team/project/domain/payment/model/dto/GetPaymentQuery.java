package com.team.project.domain.payment.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPaymentQuery {

	// 결제 ID
	private UUID id;

	// 결제 상태
	private PaymentStatus status;

	// 결제 금액
	private Integer amount;

	// 결제 완료 시각
	private LocalDateTime paidAt;

	// 생성일시
	private LocalDateTime createdAt;

	// 생성자
	private UUID createdBy;

	// 수정일시
	private LocalDateTime updatedAt;

	// 수정자
	private UUID updatedBy;

	public static GetPaymentQuery from(Payment payment) {
		return GetPaymentQuery.builder()
				.id(payment.getId())
				.status(payment.getStatus())
				.amount(payment.getAmount())
				.paidAt(payment.getPaidAt()) // 결제 완료 시각 추가
				.createdAt(payment.getCreatedAt())
				.createdBy(payment.getCreatedBy())
				.updatedAt(payment.getUpdatedAt())
				.updatedBy(payment.getUpdatedBy())
				.build();
	}
}