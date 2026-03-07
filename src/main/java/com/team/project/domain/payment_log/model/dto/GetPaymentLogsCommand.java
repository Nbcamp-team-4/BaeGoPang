package com.team.project.domain.payment_log.model.dto;

import java.time.LocalDateTime;

import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;
import com.team.project.global.common.dto.BasePageRequest;
import com.team.project.global.common.dto.BaseRangeRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPaymentLogsCommand extends BasePageRequest {

	private PaymentLogStatus status;
	private BaseRangeRequest<LocalDateTime> rangeCreatedAt;

	public GetPaymentLogsCommand(Integer page, Integer size, PaymentLogStatus status,
		BaseRangeRequest<LocalDateTime> rangeCreatedAt) {
		super(page, size);
		this.status = status;
		this.rangeCreatedAt = rangeCreatedAt;
	}

	public static GetPaymentLogsCommand of(Integer page, Integer size, PaymentLogStatus status,
		BaseRangeRequest<LocalDateTime> rangeCreatedAt) {
		return new GetPaymentLogsCommand(page, size, status, rangeCreatedAt);
	}
}
