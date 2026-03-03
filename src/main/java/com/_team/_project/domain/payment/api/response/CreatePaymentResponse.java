package com._team._project.domain.payment.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com._team._project.domain.payment.model.vo.PaymentMethod;
import com._team._project.domain.payment.model.vo.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePaymentResponse {

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

}
