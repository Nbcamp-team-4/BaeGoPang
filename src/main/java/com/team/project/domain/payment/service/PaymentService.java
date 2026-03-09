package com.team.project.domain.payment.service;

import java.util.UUID;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.payment.model.dto.CancelPaymentCommand;
import com.team.project.domain.payment.model.dto.CancelPaymentQuery;
import com.team.project.domain.payment.model.dto.CreatePaymentCommand;
import com.team.project.domain.payment.model.dto.CreatePaymentQuery;
import com.team.project.domain.payment.model.dto.GetPaymentQuery;
import com.team.project.domain.payment.model.dto.GetPaymentsCommand;
import com.team.project.domain.payment.model.dto.GetPaymentsQuery;
import com.team.project.domain.payment.model.dto.PayPaymentCommand;
import com.team.project.domain.payment.model.dto.PayPaymentQuery;

public interface PaymentService {
	CreatePaymentQuery createPayment(CreatePaymentCommand command);

	PayPaymentQuery payPayment(PayPaymentCommand command);

	CancelPaymentQuery cancelPayment(CancelPaymentCommand command);

	void deletePayment(UUID paymentId, UserDto userDto);

	GetPaymentQuery getPayment(UUID paymentId);

	GetPaymentsQuery getPayments(GetPaymentsCommand command);
}
