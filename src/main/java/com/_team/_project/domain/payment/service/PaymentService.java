package com._team._project.domain.payment.service;

import com._team._project.domain.payment.api.request.CreatePaymentRequest;
import com._team._project.domain.payment.api.response.CreatePaymentResponse;

public interface PaymentService {
	CreatePaymentResponse createPayment(CreatePaymentRequest request);
}
