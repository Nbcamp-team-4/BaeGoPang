package com._team._project.domain.order.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com._team._project.domain.order.entity.Order;

public interface OrderRepositoryCustom {

    // 주문 상세(아이템/옵션 포함)
    Optional<Order> findDetailById(UUID orderId);

    // 내 주문 상세(권한 체크용)
    Optional<Order> findDetailByIdAndUserId(UUID orderId, UUID userId);

    // 내 주문 목록 (간단 버전: 페이징은 Pageable 추가할 예정)
    List<Order> findAllByUserIdOrderByOrderDateDesc(UUID userId);
}