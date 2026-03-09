package com.team.project.domain.payment_log.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.domain.payment_log.model.dto.GetPaymentLogQuery;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "결제 로그 조회 응답")
public class GetPaymentLogResponse {

	@Schema(description = "결제 ID", format = "uuid")
	private UUID id;
	@Schema(description = "결제 로그 키", format = "sdfqdrewersdf")
	private String paymentKey;
	@Schema(description = "결제 로그 상태", format = "PAY_SUCCESS")
	private PaymentLogStatus status;
	@Schema(description = "실패 이유", format = "INTERNAL_SERVER_ERROR")
	private String reason;
	@Schema(description = "결제 상태", format = "PAID")
	private PaymentStatus paymentStatus;
	@Schema(description = "결제 금액", format = "15000")
	private Integer paymentAmount;
	@Schema(description = "결제 시각")
	private LocalDateTime paidAt;
	@Schema(description = "생성 시각")
	private LocalDateTime createdAt;
	@Schema(description = "생성자 ID", format = "uuid")
	private UUID createdBy;
	@Schema(description = "수정 시각")
	private LocalDateTime updatedAt;
	@Schema(description = "수정자 ID", format = "uuid")
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
