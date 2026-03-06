package com.team.project.domain.order.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.team.project.domain.order.api.request.CancelOrderRequest;
import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.repository.OrderRepository;
import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.domain.payment.repository.PaymentRepository;
import com.team.project.domain.payment.service.PaymentService;
import com.team.project.domain.product.repository.ProductRepository;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.repository.StoreRepository;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.repository.UserAddressRepository;
import com.team.project.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class OrderPaymentIntegrationTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserAddressRepository userAddressRepository;

    private UUID userId;
    private UUID storeId;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        // 테스트마다 사용할 고정 UUID를 미리 생성한다.
        userId = UUID.randomUUID();
        storeId = UUID.randomUUID();
        orderId = UUID.randomUUID();
    }

    @Test
    @DisplayName("결제 완료 주문 취소 시 환불 로직 호출 테스트")
    void cancelPaidOrder_shouldCallRefundFlow() {
        // =========================
        // given
        // =========================

        // User 엔티티는 id 필드만 필요하므로 기본 생성 후 Reflection으로 id를 주입한다.
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);

        // Store 엔티티는 public 생성자를 사용해서 생성한다.
        // location(Point)은 테스트에서 실제 사용하지 않으므로 null로 둔다.
        Store store = new Store(
                UUID.randomUUID(),   // userId
                UUID.randomUUID(),   // regionId
                "테스트가게",
                "테스트 설명",
                "서울시 강남구",
                (Point) null,
                "010-1234-5678",
                null,
                LocalTime.of(9, 0),
                LocalTime.of(21, 0),
                20,
                40,
                3000,
                15000
        );
        ReflectionTestUtils.setField(store, "id", storeId);

        // 주문 생성 직후 상태는 PENDING_PAYMENT 이므로
        // markPaid()를 호출해서 PAID 상태로 만든다.
        Order order = new Order(
                user,
                store,
                null,
                "ORD-TEST-001",
                20000,
                "문 앞에 놓아주세요"
        );
        ReflectionTestUtils.setField(order, "id", orderId);
        order.markPaid();

        // 최신 결제는 COMPLETED 상태로 만들어서
        // 주문 취소 시 환불 로직이 타도록 한다.
        Payment payment = Payment.builder()
                .status(PaymentStatus.COMPLETED)
                .amount(20000)
                .paymentKey("test-payment-key-1")
                .order(order)
                .build();

        CancelOrderRequest request = new CancelOrderRequest();
        request.setReason("고객 단순 변심");

        // 주문 조회 시 위에서 만든 order를 반환하도록 설정
        when(orderRepository.findDetailById(orderId)).thenReturn(Optional.of(order));

        // cancelOrder() 내부에서는 최신 결제를 최소 2번 조회할 수 있다.
        // 1) 취소 전 결제 상태 확인
        // 2) 취소 후 응답 생성용 재조회
        when(paymentRepository.getLatestPaymentByOrderId(orderId))
                .thenReturn(Optional.of(payment), Optional.of(payment));

        // =========================
        // when
        // =========================
        orderService.cancelOrder(orderId, userId, request);

        // =========================
        // then
        // =========================

        // 결제 완료 주문 취소 시
        // 결제 취소 요청과 결제 취소가 각각 1번씩 호출되어야 한다.
        verify(paymentService, times(1)).requestCancelPayment(any());
        verify(paymentService, times(1)).cancelPayment(any());
    }

    @Test
    @DisplayName("결제 완료 주문 거절 시 환불 로직 호출 테스트")
    void rejectPaidOrder_shouldCallRefundFlow() {
        // =========================
        // given
        // =========================

        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);

        Store store = new Store(
                UUID.randomUUID(),   // userId
                UUID.randomUUID(),   // regionId
                "테스트가게",
                "테스트 설명",
                "서울시 송파구",
                (Point) null,
                "010-9999-0000",
                null,
                LocalTime.of(10, 0),
                LocalTime.of(22, 0),
                15,
                35,
                2500,
                12000
        );
        ReflectionTestUtils.setField(store, "id", storeId);

        // 주문을 생성하고 결제 완료 전 상태인 PAID로 만든다.
        // rejectOrder()는 주문 상태가 PAID일 때만 허용된다.
        Order order = new Order(
                user,
                store,
                null,
                "ORD-TEST-002",
                18000,
                "빠르게 부탁드립니다"
        );
        ReflectionTestUtils.setField(order, "id", orderId);
        order.markPaid();

        Payment payment = Payment.builder()
                .status(PaymentStatus.COMPLETED)
                .amount(18000)
                .paymentKey("test-payment-key-2")
                .order(order)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // rejectOrder() 내부에서도 최신 결제를 2번 조회할 수 있으므로
        // 동일 객체를 연속 반환하도록 설정한다.
        when(paymentRepository.getLatestPaymentByOrderId(orderId))
                .thenReturn(Optional.of(payment), Optional.of(payment));

        // =========================
        // when
        // =========================
        orderService.rejectOrder(orderId, storeId, "재고 부족");

        // =========================
        // then
        // =========================

        // 가게 거절 시에도 결제 완료 상태라면
        // 환불 흐름(requestCancel + cancel)이 호출되어야 한다.
        verify(paymentService, times(1)).requestCancelPayment(any());
        verify(paymentService, times(1)).cancelPayment(any());
    }
}