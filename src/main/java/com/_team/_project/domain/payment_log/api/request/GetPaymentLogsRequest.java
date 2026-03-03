package com._team._project.domain.payment_log.api.request;

import java.time.LocalDateTime;

import com._team._project.domain.payment_log.model.vo.PaymentLogStatus;
import com._team._project.global.common.dto.BasePageRequest;
import com._team._project.global.common.dto.BaseRangeRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPaymentLogsRequest extends BasePageRequest {

	private PaymentLogStatus status;
	private BaseRangeRequest<LocalDateTime> rangeCreatedAt;

	public GetPaymentLogsRequest(Integer page, Integer size, PaymentLogStatus status,
		BaseRangeRequest<LocalDateTime> rangeCreatedAt) {
		super(page, size);
		this.status = status;
		this.rangeCreatedAt = rangeCreatedAt;
	}
}
