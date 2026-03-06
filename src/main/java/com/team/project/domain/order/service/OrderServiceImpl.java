package com.team.project.domain.order.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.team.project.domain.product.entity.Product;
import com.team.project.domain.user.entity.UserAddress;
import com.team.project.domain.user.repository.UserAddressRepository;
import org.springframework.stereotype.Service;

import com.team.project.domain.order.api.request.CancelOrderRequest;
import com.team.project.domain.order.api.request.CreateOrderRequest;
import com.team.project.domain.order.api.request.UpdateOrderStatusRequest;
import com.team.project.domain.order.api.response.CancelOrderResponse;
import com.team.project.domain.order.api.response.CreateOrderResponse;
import com.team.project.domain.order.api.response.GetOrderDetailResponse;
import com.team.project.domain.order.api.response.GetOrderSummaryResponse;
import com.team.project.domain.order.api.response.UpdateOrderStatusResponse;
import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.entity.OrderItem;
import com.team.project.domain.order.entity.OrderItemOption;
import com.team.project.domain.order.exception.InvalidOrderStatusException;
import com.team.project.domain.order.exception.OrderAlreadyCanceledException;
import com.team.project.domain.order.exception.OrderCannotCancelException;
import com.team.project.domain.order.exception.OrderForbiddenException;
import com.team.project.domain.order.exception.OrderNotFoundException;
import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.domain.order.repository.OrderRepository;
import com.team.project.domain.product.repository.ProductRepository;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.repository.StoreRepository;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    // 주문 생성 시 FK(유저/가게/상품) 확인을 위해 필요
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final UserAddressRepository userAddressRepository;

    // ======================
    // customer
    // ======================

    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {

        // 1) 유저/가게 존재 확인 (TODO: UserNotFoundException/StoreNotFoundException 권장)
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("STORE_NOT_FOUND"));

        // 2) 주문번호 생성
        String orderNo = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 3) 총액 계산 (요청 unitPrice/extraPrice 기반)
        int totalAmount = request.getItems().stream()
                .mapToInt(item -> {
                    int optionSum = 0;
                    if (item.getOptions() != null) {
                        optionSum = item.getOptions().stream()
                                .mapToInt(opt -> opt.getExtraPrice() == null ? 0 : opt.getExtraPrice())
                                .sum();
                    }
                    return (item.getUnitPrice() + optionSum) * item.getQuantity();
                })
                .sum();

        // 4) 배송지 조회 로직 추가
        UserAddress deliveryAddress = null;

        if (request.getDeliveryAddressId() != null) {
            // 내 배송지인지까지 확인하려면 findByIdAndUserId 사용
            deliveryAddress = userAddressRepository
                    .findByIdAndUserId(request.getDeliveryAddressId(), request.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("DELIVERY_ADDRESS_NOT_FOUND"));
        }

        // 5) Order 생성
        Order order = new Order(
                user,
                store,
                deliveryAddress,
                orderNo,
                totalAmount,
                request.getRequestMemo()
        );

        // 6) OrderItem/Option 생성 후 연결
        for (CreateOrderRequest.CreateOrderItemRequest itemReq : request.getItems()) {

            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("PRODUCT_NOT_FOUND"));

            OrderItem orderItem = new OrderItem(
                    product,
                    itemReq.getProductName(),
                    itemReq.getUnitPrice(),
                    itemReq.getQuantity()
            );

            if (itemReq.getOptions() != null) {
                for (CreateOrderRequest.CreateOrderItemOptionRequest optReq : itemReq.getOptions()) {
                    OrderItemOption option = new OrderItemOption(
                            optReq.getOptionName(),
                            optReq.getOptionItemName(),
                            optReq.getExtraPrice()
                    );
                    orderItem.addOption(option);
                }
            }

            order.addItem(orderItem);
        }

        // 7) 저장
        Order saved = orderRepository.save(order);

        // 8) 응답 반환
        return CreateOrderResponse.from(saved);
    }

    @Override
    public GetOrderDetailResponse getOrderDetail(UUID orderId, UUID userId) {

        // 1) 내 주문 상세 조회(아이템/옵션 포함)
        Order order = orderRepository.findDetailByIdAndUserId(orderId, userId)
                .orElseThrow(OrderNotFoundException::new);

        // 2) 응답 변환
        return GetOrderDetailResponse.from(order);
    }

    @Override
    public List<GetOrderSummaryResponse> getMyOrders(UUID userId) {

        // 1) 내 주문 목록 조회
        List<Order> orders = orderRepository.findAllByUserIdOrderByOrderDateDesc(userId);

        // 2) 응답 변환
        return orders.stream()
                .map(GetOrderSummaryResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CancelOrderResponse cancelOrder(UUID orderId, UUID userId, CancelOrderRequest request) {

        // 1) 주문 조회
        Order order = orderRepository.findDetailById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        // 2) 본인 주문인지 확인
        if (!order.getUser().getId().equals(userId)) {
            throw new OrderForbiddenException();
        }

        // 3) 이미 취소인지 확인
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new OrderAlreadyCanceledException();
        }

        // 4) 취소 가능 상태인지 확인
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new OrderCannotCancelException();
        }
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.PAID) {
            throw new InvalidOrderStatusException();
        }

        // 5) 취소 처리
        order.cancel(request.getReason());

        // 6) 응답 반환
        return CancelOrderResponse.from(order);
    }

    @Override
    @Transactional
    public void deleteOrder(UUID orderId, UUID userId) {

        // 1) 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        // 2) 본인 주문인지 확인
        if (!order.getUser().getId().equals(userId)) {
            throw new OrderForbiddenException();
        }

        // 3) 소프트 삭제
        order.markDeleted(userId);
    }

    // ======================
    // manager (store)
    // ======================

    @Override
    public List<GetOrderSummaryResponse> getStoreOrders(UUID storeId) {

        // 1) 가게 주문 목록 조회
        // ⚠️ 필요 리포지토리 메서드: findAllByStoreIdOrderByOrderDateDesc(storeId)
        List<Order> orders = orderRepository.findAllByStoreIdOrderByOrderDateDesc(storeId);

        // 2) 응답 변환
        return orders.stream()
                .map(GetOrderSummaryResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public GetOrderDetailResponse getStoreOrderDetail(UUID orderId, UUID storeId) {

        // 1) 가게 주문 상세 조회(가게 소유 확인)
        // ⚠️ 필요 리포지토리 메서드: findDetailByIdAndStoreId(orderId, storeId)
        Order order = orderRepository.findDetailByIdAndStoreId(orderId, storeId)
                .orElseThrow(OrderNotFoundException::new);

        // 2) 응답 변환
        return GetOrderDetailResponse.from(order);
    }

    @Override
    @Transactional
    public UpdateOrderStatusResponse acceptOrder(UUID orderId, UUID storeId) {

        // 1) 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        // 2) 해당 가게 주문인지 확인
        if (!order.getStore().getId().equals(storeId)) {
            throw new OrderForbiddenException();
        }

        // 3) 수락 가능한 상태인지 확인 (임시: PENDING만 허용)
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException();
        }

        // 4) 상태 변경 (임시 구현: 수락 -> PAID)
        order.markPaid();

        return UpdateOrderStatusResponse.from(order);
    }

    @Override
    @Transactional
    public UpdateOrderStatusResponse rejectOrder(UUID orderId, UUID storeId, String reason) {

        // 1) 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        // 2) 해당 가게 주문인지 확인
        if (!order.getStore().getId().equals(storeId)) {
            throw new OrderForbiddenException();
        }

        // 3) 거절 가능한 상태인지 확인 (임시: PENDING만 허용)
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException();
        }

        // 4) 거절 처리 (임시 구현: 거절 -> CANCELED)
        order.cancel(reason);

        return UpdateOrderStatusResponse.from(order);
    }

    @Override
    @Transactional
    public UpdateOrderStatusResponse updateOrderStatusByStore(UUID orderId, UUID storeId, UpdateOrderStatusRequest request) {

        // 1) 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        // 2) 해당 가게 주문인지 확인
        if (!order.getStore().getId().equals(storeId)) {
            throw new OrderForbiddenException();
        }

        // 3) 상태 변경 (현재 enum 범위 내에서만 허용)
        OrderStatus target = request.getStatus();

        // 예: 가게는 COMPLETED로만 마감 처리 가능(정책은 팀 룰에 맞게 조정)
        if (target == OrderStatus.COMPLETED) {
            order.complete();
        } else if (target == OrderStatus.PAID) {
            order.markPaid();
        } else if (target == OrderStatus.CANCELED) {
            order.cancel("STORE_STATUS_UPDATE");
        } else {
            throw new InvalidOrderStatusException();
        }

        return UpdateOrderStatusResponse.from(order);
    }
}