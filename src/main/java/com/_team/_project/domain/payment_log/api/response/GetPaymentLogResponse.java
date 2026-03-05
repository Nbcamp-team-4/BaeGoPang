package com._team._project.domain.payment_log.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com._team._project.domain.payment.model.vo.PaymentMethod;
import com._team._project.domain.payment.model.vo.PaymentStatus;
import com._team._project.domain.payment_log.model.vo.PaymentLogStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPaymentLogResponse {

	private UUID id;
	private String tid;
	private PaymentLogStatus status;
	private String reason;

	private PaymentMethod paymentMethod;
	private PaymentStatus paymentStatus;
	private Integer paymentAmount;
	private LocalDateTime paidAt;

	private UUID pgProviderId;
	private String pgProviderName;
	private String pgProviderCode;

	private LocalDateTime createdAt;
	private UUID createdBy;

}
