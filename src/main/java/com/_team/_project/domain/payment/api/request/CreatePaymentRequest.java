package com._team._project.domain.payment.api.request;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com._team._project.domain.payment.model.vo.PaymentMethod;

import lombok.Data;

@Data
public class CreatePaymentRequest {

	@NotNull
	private UUID orderId;
	@NotNull
	private PaymentMethod method;
	@NotNull
	private Integer amount;

	private UUID pgProviderId;
	private String pgTid;
}
