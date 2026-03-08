package com.team.project.domain.order.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.team.project.domain.order.exception.OrderPaymentCancelFailedException;
import com.team.project.domain.order.exception.OrderPaymentNotFoundException;
import com.team.project.domain.order.exception.OrderPaymentRefundFailedException;
import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.exception.InvalidPaymentRequestException;
import com.team.project.domain.payment.exception.PaymentNotFoundException;
import com.team.project.domain.payment.model.dto.CancelPaymentCommand;
import com.team.project.domain.payment.model.dto.CancelPaymentQuery;
import com.team.project.domain.payment.repository.PaymentRepository;
import com.team.project.domain.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderPaymentProcessor {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public CancelPaymentQuery cancelForOrder(UUID orderId, String reason) {
        try {
            return paymentService.cancelPayment(
                    CancelPaymentCommand.ofCancel(orderId, reason)
            );
        } catch (PaymentNotFoundException e) {
            throw new OrderPaymentNotFoundException();
        } catch (InvalidPaymentRequestException e) {
            throw new OrderPaymentCancelFailedException("주문 취소에 대한 결제 취소가 불가능합니다.");
        } catch (Exception e) {
            throw new OrderPaymentCancelFailedException("주문 취소 중 결제 취소 처리에 실패했습니다.");
        }
    }

    public CancelPaymentQuery refundForRejectedOrder(UUID orderId, String reason) {
        try {
            return paymentService.cancelPayment(
                    CancelPaymentCommand.ofRefund(orderId, reason)
            );
        } catch (PaymentNotFoundException e) {
            throw new OrderPaymentNotFoundException();
        } catch (InvalidPaymentRequestException e) {
            throw new OrderPaymentRefundFailedException("주문 거절에 대한 환불 처리가 불가능합니다.");
        } catch (Exception e) {
            throw new OrderPaymentRefundFailedException("주문 거절 중 환불 처리에 실패했습니다.");
        }
    }

    public Payment getLatestPaymentOrNull(UUID orderId) {
        return paymentRepository.getLatestPaymentByOrderId(orderId).orElse(null);
    }
}