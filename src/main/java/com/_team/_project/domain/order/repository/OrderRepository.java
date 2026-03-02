package com._team._project.domain.order.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com._team._project.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID>, OrderRepositoryCustom {
    // 기본 CRUD는 JpaRepository가 제공
    // 커스텀은 OrderRepositoryCustom에 선언
}