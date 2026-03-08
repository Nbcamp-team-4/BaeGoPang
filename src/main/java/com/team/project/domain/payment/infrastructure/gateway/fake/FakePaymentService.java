package com.team.project.domain.payment.infrastructure.gateway.fake;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.team.project.domain.payment.infrastructure.PgProviderService;
import com.team.project.domain.payment.infrastructure.dto.CancelPgProviderPaymentCommand;
import com.team.project.domain.payment.infrastructure.dto.CancelPgProviderPaymentQuery;
import com.team.project.domain.payment.infrastructure.dto.ConfirmPgProviderPaymentCommand;
import com.team.project.domain.payment.infrastructure.dto.ConfirmPgProviderPaymentQuery;

import lombok.extern.slf4j.Slf4j;

@Service
@Profile("local")
@Slf4j
public class FakePaymentService implements PgProviderService {

    @Override
    public ConfirmPgProviderPaymentQuery confirmPayment(ConfirmPgProviderPaymentCommand command) {

        log.info("Fake PG confirmPayment 호출");

        return ConfirmPgProviderPaymentQuery.builder()
                .paymentKey(command.getPaymentKey())
                .orderId(UUID.fromString(command.getOrderId()))
                .amount(command.getAmount())
                .build();
    }

    @Override
    public CancelPgProviderPaymentQuery cancelPayment(CancelPgProviderPaymentCommand command) {

        log.info("Fake PG cancelPayment 호출");

        return CancelPgProviderPaymentQuery.builder()
                .paymentKey(command.getPaymentKey())
                .status("CANCELED")
                .cancelReason(command.getReason())
                .cancelAmount(0)
                .canceledAt(LocalDateTime.now().toString())
                .build();
    }
}