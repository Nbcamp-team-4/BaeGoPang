package com.team.project.domain.payment.api.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.global.common.dto.BasePageRequest;
import com.team.project.global.common.dto.BaseRangeRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPaymentsRequest extends BasePageRequest {

	@Schema(description = "결제 상태", example = "PAID")
	private PaymentStatus paymentStatus;
	@Schema(description = "결제 금액 범위")
	private BaseRangeRequest<Integer> rangeAmount;
	@Schema(description = "결제 시간 범위")
	private BaseRangeRequest<LocalDateTime> rangePaidAt;
	@Schema(description = "주문 ID", format = "uuid")
	private UUID orderId;
}
