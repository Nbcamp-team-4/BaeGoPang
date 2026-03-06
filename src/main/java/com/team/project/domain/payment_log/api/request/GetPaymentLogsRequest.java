package com.team.project.domain.payment_log.api.request;

import java.time.LocalDateTime;

import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;
import com.team.project.global.common.dto.BaseRangeRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPaymentLogsRequest {

	private Integer page;
	private Integer size;
	private PaymentLogStatus status;
	private BaseRangeRequest<LocalDateTime> rangeCreatedAt;

	public GetPaymentLogsRequest(Integer page, Integer size, PaymentLogStatus status,
		BaseRangeRequest<LocalDateTime> rangeCreatedAt) {
		this.page = page;
		this.size = size;
		this.status = status;
		this.rangeCreatedAt = rangeCreatedAt;
	}
}
