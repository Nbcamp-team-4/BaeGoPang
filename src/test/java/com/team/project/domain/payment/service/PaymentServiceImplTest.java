package com.team.project.domain.payment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.domain.order.repository.OrderRepository;
import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.exception.PaymentAlreadyPaidException;
import com.team.project.domain.payment.infrastructure.PgProviderService;
import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.domain.payment.repository.PaymentRepository;
import com.team.project.domain.payment_log.entity.PaymentLog;
import com.team.project.domain.payment_log.repository.PaymentLogRepository;
import com.team.project.global.common.exception.BaseException;

/**
 * 주의:
 * 아래 테스트는 네 프로젝트 구조 기준 통합 템플릿이다.
 * 실제 컴파일 시 아래 메서드 시그니처만 네 코드에 맞춰 맞추면 된다.
 *
 * 1) paymentService.approvePayment(orderId, paymentKey, amount)
 * 2) paymentService.cancelPayment(orderId, reason)
 * 3) paymentRepository.findTopByOrderIdOrderByCreatedAtDesc(orderId)
 * 4) paymentRepository.existsByOrderIdAndStatusIn(orderId, statuses)
 */
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentLogRepository paymentLogRepository;

    @Mock
    private PgProviderService pgProviderService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private UUID orderId;
    private UUID paymentId;

    @BeforeEach
    void setUp() {
        orderId = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");
        paymentId = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");
    }

    @DisplayName("결제 승인 실패 시 Payment 상태는 FAILED가 되고 Order 상태는 완료되지 않는다")
    @Test
    void approvePayment_fail_shouldMarkFailed() {
        // given
        Order order = createOrder(orderId, OrderStatus.PENDING_PAYMENT);
        Payment payment = createPayment(paymentId, order, PaymentStatus.READY, 36000);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.findTopByOrderIdOrderByCreatedAtDesc(orderId))
                .thenReturn(Optional.of(payment));

        // PG 승인 실패 가정
        when(pgProviderService.approve(any()))
                .thenThrow(new BaseException("PG 승인 실패"));

        // when & then
        assertThatThrownBy(() -> paymentService.approvePayment(orderId, "FAIL-TEST-KEY", 36000))
                .isInstanceOf(BaseException.class);

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING_PAYMENT);

        verify(paymentRepository).save(payment);
        verify(paymentLogRepository).save(any(PaymentLog.class));
    }

    @DisplayName("결제 취소 성공 시 Payment 상태는 CANCELED가 되고 Order 상태는 CANCELED가 된다")
    @Test
    void cancelPayment_success_shouldCancelBothPaymentAndOrder() {
        // given
        Order order = createOrder(orderId, OrderStatus.PAID);
        Payment payment = createPayment(paymentId, order, PaymentStatus.PAID, 36000);

        setField(payment, "paymentKey", "PAID-TEST-KEY");
        setField(payment, "paidAt", LocalDateTime.now());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.findTopByOrderIdOrderByCreatedAtDesc(orderId))
                .thenReturn(Optional.of(payment));

        when(pgProviderService.cancel(any()))
                .thenReturn(Boolean.TRUE);

        // when
        paymentService.cancelPayment(orderId, "고객 요청");

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);

        verify(paymentRepository).save(payment);
        verify(paymentLogRepository).save(any(PaymentLog.class));
    }

    @DisplayName("이미 결제 완료된 주문은 중복 결제할 수 없다")
    @Test
    void approvePayment_duplicate_shouldThrowException() {
        // given
        Order order = createOrder(orderId, OrderStatus.PAID);
        Payment payment = createPayment(paymentId, order, PaymentStatus.PAID, 36000);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.findTopByOrderIdOrderByCreatedAtDesc(orderId))
                .thenReturn(Optional.of(payment));

        // when & then
        assertThatThrownBy(() -> paymentService.approvePayment(orderId, "DUPLICATE-KEY", 36000))
                .isInstanceOfAny(PaymentAlreadyPaidException.class, BaseException.class);

        verify(paymentRepository, never()).save(any(Payment.class));
        verify(paymentLogRepository).save(any(PaymentLog.class));
    }

    @DisplayName("진행 중인 결제가 이미 있으면 새 결제를 생성할 수 없다")
    @Test
    void createPayment_whenReadyPaymentExists_shouldThrowException() {
        // given
        Order order = createOrder(orderId, OrderStatus.PENDING_PAYMENT);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.existsByOrderIdAndStatusIn(
                eq(orderId),
                anyCollection()
        )).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> paymentService.createPayment(orderId, 36000))
                .isInstanceOf(BaseException.class);

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    private Order createOrder(UUID id, OrderStatus status) {
        Order order = Order.builder().build();
        setField(order, "id", id);
        setField(order, "status", status);
        return order;
    }

    private Payment createPayment(UUID id, Order order, PaymentStatus status, int amount) {
        Payment payment = Payment.builder().build();
        setField(payment, "id", id);
        setField(payment, "order", order);
        setField(payment, "status", status);
        setField(payment, "amount", amount);
        return payment;
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = findField(target.getClass(), fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(
                    "필드 세팅 실패 - 클래스: " + target.getClass().getSimpleName() + ", 필드: " + fieldName,
                    e
            );
        }
    }

    private Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}