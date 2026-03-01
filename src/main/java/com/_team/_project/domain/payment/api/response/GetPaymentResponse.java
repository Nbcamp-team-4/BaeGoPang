package com._team._project.domain.payment.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com._team._project.domain.payment.entity.Payment;
import com._team._project.domain.payment.model.vo.PaymentMethod;
import com._team._project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPaymentResponse {
	private UUID id;
	private PaymentStatus status;
	private PaymentMethod method;
	private Integer amount;
	private String orderNo;
	private String orderStatus;
	private String pgCode;
	private String pgName;
	private LocalDateTime createdAt;
	private UUID createdBy;
	private LocalDateTime updatedAt;
	private UUID updatedBy;

	public static GetPaymentResponse from(Payment payment) {
		return GetPaymentResponse.builder()
			.id(payment.getId())
			.status(payment.getStatus())
			.method(payment.getMethod())
			.amount(payment.getAmount())
			.orderNo("test")
			.orderStatus("test_status")
			.pgCode("test_pg_code")
			.pgName("test_pg_name")
			.createdAt(payment.getCreatedAt())
			.createdBy(payment.getCreatedBy())
			.updatedAt(payment.getUpdatedAt())
			.updatedBy(payment.getUpdatedBy())
			.build();
	}
}
