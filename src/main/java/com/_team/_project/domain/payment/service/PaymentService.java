package com._team._project.domain.payment.service;

import java.util.UUID;

import com._team._project.domain.payment.api.request.CreatePaymentRequest;
import com._team._project.domain.payment.api.response.CancelPaymentResponse;
import com._team._project.domain.payment.api.response.CreatePaymentResponse;
import com._team._project.domain.payment.api.response.PayPaymentResponse;

public interface PaymentService {
	CreatePaymentResponse createPayment(CreatePaymentRequest request);

	PayPaymentResponse payPayment(UUID paymentId);

	CancelPaymentResponse cancelPayment(UUID paymentId);

	void deletePayment(UUID paymentId);
}
