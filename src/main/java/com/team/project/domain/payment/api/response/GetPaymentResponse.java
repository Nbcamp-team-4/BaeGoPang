package com.team.project.domain.payment.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import com.team.project.domain.payment.model.dto.GetPaymentQuery;
import com.team.project.domain.payment.model.vo.PaymentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "결제 조회 응답")
public class GetPaymentResponse {
	@Schema(description = "결제 ID", format = "uuid")
	private UUID id;
	@Schema(description = "결제 상태", example = "PAID")
	private PaymentStatus status;
	@Schema(description = "결제 금액", example = "15000")
	private Integer amount;
	@Schema(description = "주문 번호", example = "ORD-20250309-001")
	private String orderNo;
	@Schema(description = "주문 상태", example = "ORDERED")
	private String orderStatus;
	@Schema(description = "PG 코드", example = "KAKAOPAY")
	private String pgCode;
	@Schema(description = "PG 이름", example = "카카오페이")
	private String pgName;
	@Schema(description = "생성 시각")
	private LocalDateTime createdAt;
	@Schema(description = "생성자 ID", format = "uuid")
	private UUID createdBy;
	@Schema(description = "수정 시각")
	private LocalDateTime updatedAt;
	@Schema(description = "수정자 ID", format = "uuid")
	private UUID updatedBy;

	public static GetPaymentResponse from(@MonotonicNonNull GetPaymentQuery payment) {
		return GetPaymentResponse.builder()
			.id(payment.getId())
			.status(payment.getStatus())
			.amount(payment.getAmount())
			.createdAt(payment.getCreatedAt())
			.createdBy(payment.getCreatedBy())
			.updatedAt(payment.getUpdatedAt())
			.updatedBy(payment.getUpdatedBy())
			.build();
	}
}
