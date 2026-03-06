package com.team.project.domain.order.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.team.project.domain.order.api.request.CancelOrderRequest;
import com.team.project.domain.order.api.request.UpdateOrderStatusRequest;
import com.team.project.domain.order.api.response.CancelOrderResponse;
import com.team.project.domain.order.api.response.GetOrderDetailResponse;
import com.team.project.domain.order.api.response.GetOrderSummaryResponse;
import com.team.project.domain.order.api.response.UpdateOrderStatusResponse;
import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.exception.InvalidOrderStatusException;
import com.team.project.domain.order.exception.OrderNotFoundException;
import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.domain.order.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderAdminServiceImpl implements OrderAdminService {

    private final OrderRepository orderRepository;

    @Override
    public List<GetOrderSummaryResponse> getAllOrders() {

        // 1) 전체 조회 (운영에서는 페이징 필수)
        List<Order> orders = orderRepository.findAll();

        // 2) 응답 변환
        return orders.stream()
                .map(GetOrderSummaryResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public GetOrderDetailResponse getOrderDetail(UUID orderId) {

        // 1) 상세 조회(아이템/옵션 포함)
        Order order = orderRepository.findDetailById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        return GetOrderDetailResponse.from(order);
    }

    @Override
    @Transactional
    public CancelOrderResponse cancelOrder(UUID orderId, CancelOrderRequest request) {

        // 1) 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        // 2) 강제 취소 (상태 체크 정책은 운영 룰에 맞게 조정 가능)
        order.cancel(request.getReason());

        return CancelOrderResponse.from(order);
    }

    @Override
    @Transactional
    public UpdateOrderStatusResponse updateOrderStatus(UUID orderId, UpdateOrderStatusRequest request) {

        // 1) 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        // 2) 강제 상태 변경 (현재 enum 범위 내)
        OrderStatus target = request.getStatus();

        if (target == OrderStatus.COMPLETED) {
            order.complete();
        } else if (target == OrderStatus.PAID) {
            order.markPaid();
        } else if (target == OrderStatus.CANCELED) {
            order.cancel("ADMIN_FORCE_CANCEL");
        } else if (target == OrderStatus.PENDING) {
            // PENDING으로 되돌리는 건 정책적으로 막는 게 보통이지만, 필요하면 허용
            throw new InvalidOrderStatusException();
        }

        return UpdateOrderStatusResponse.from(order);
    }
}