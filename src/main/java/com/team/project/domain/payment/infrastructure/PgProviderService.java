package com.team.project.domain.payment.infrastructure;

import com.team.project.domain.payment.infrastructure.dto.CancelPgProviderPaymentCommand;
import com.team.project.domain.payment.infrastructure.dto.CancelPgProviderPaymentQuery;
import com.team.project.domain.payment.infrastructure.dto.ConfirmPgProviderPaymentCommand;
import com.team.project.domain.payment.infrastructure.dto.ConfirmPgProviderPaymentQuery;

public interface PgProviderService {

	ConfirmPgProviderPaymentQuery confirmPayment(ConfirmPgProviderPaymentCommand command);

	CancelPgProviderPaymentQuery cancelPayment(CancelPgProviderPaymentCommand command);
}
