package com.team.project.domain.order.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.global.common.dto.BaseRangeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {

    // 주문 상세(아이템/옵션 포함)
    Optional<Order> findDetailById(UUID orderId);

    // 내 주문 상세(권한 체크용)
    Optional<Order> findDetailByIdAndUserId(UUID orderId, UUID userId);

    // 내 주문 목록
    List<Order> findAllByUserIdOrderByOrderDateDesc(UUID userId);

    // ======================
    // store (manager)
    // ======================

    // 가게 주문 목록
    List<Order> findAllByStoreIdOrderByOrderDateDesc(UUID storeId);

    // 가게 주문 상세(해당 가게 주문인지 확인용)
    Optional<Order> findDetailByIdAndStoreId(UUID orderId, UUID storeId);

    Page<Order> searchMyOrders(
            UUID userId,
            OrderStatus status,
            BaseRangeRequest<LocalDateTime> rangeCreatedAt,
            Pageable pageable
    );

    Page<Order> searchStoreOrders(
            UUID storeId,
            OrderStatus status,
            BaseRangeRequest<LocalDateTime> rangeCreatedAt,
            Pageable pageable
    );

    Page<Order> searchAdminOrders(
            UUID storeId,
            UUID userId,
            OrderStatus status,
            BaseRangeRequest<LocalDateTime> rangeCreatedAt,
            Pageable pageable
    );


}