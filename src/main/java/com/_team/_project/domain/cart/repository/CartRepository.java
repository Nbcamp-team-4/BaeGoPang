package com._team._project.domain.cart.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com._team._project.domain.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, UUID>, CartRepositoryCustom {
    // 기본 CRUD(save/findById/delete 등)는 JpaRepository가 제공
    // fetch join 등 복잡한 조회는 CartRepositoryCustom에 선언
}