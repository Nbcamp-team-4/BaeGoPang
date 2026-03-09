package com.team.project.domain.payment_log.api.request;

import java.time.LocalDateTime;

import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;
import com.team.project.global.common.dto.BasePageRequest;
import com.team.project.global.common.dto.BaseRangeRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPaymentLogsRequest extends BasePageRequest {

	@Schema(description = "결제 로그 상태", example = "PAY_SUCCESS")
	private PaymentLogStatus status;
	@Schema(description = "생성 시간 범위")
	private BaseRangeRequest<LocalDateTime> rangeCreatedAt;

}
