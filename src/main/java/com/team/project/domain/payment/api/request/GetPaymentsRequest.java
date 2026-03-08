package com.team.project.domain.payment.api.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.global.common.dto.BaseRangeRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPaymentsRequest {
	private Integer page;
	private Integer size;
	private PaymentStatus paymentStatus;
	private BaseRangeRequest<Integer> rangeAmount;
	private BaseRangeRequest<LocalDateTime> rangePaidAt;
	private UUID orderId;
}
