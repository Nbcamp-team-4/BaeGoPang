package com.team.project.domain.payment.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.global.common.dto.BaseRangeRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetPaymentsCommand {
	private Integer page;
	private Integer size;
	private PaymentStatus paymentStatus;
	private BaseRangeRequest<Integer> rangeAmount;
	private BaseRangeRequest<LocalDateTime> rangePaidAt;
	private UUID orderId;

	public static GetPaymentsCommand of(Integer page, Integer size, PaymentStatus paymentStatus,
		BaseRangeRequest<Integer> rangeAmount, BaseRangeRequest<LocalDateTime> rangePaidAt, UUID orderId) {
		return new GetPaymentsCommand(page, size, paymentStatus, rangeAmount, rangePaidAt, orderId);
	}
}
