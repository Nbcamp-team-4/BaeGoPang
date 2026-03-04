package com._team._project.domain.cart.repository;

import java.util.Optional;
import java.util.UUID;

import com._team._project.domain.cart.entity.Cart;

public interface CartRepositoryCustom {

    // 유저의 "활성(Active) 장바구니" 상세 조회 (items/options 포함)
    Optional<Cart> findActiveCartDetailByUserId(UUID userId);

    // cartId로 장바구니 상세 조회 (items/options 포함)
    Optional<Cart> findCartDetailById(UUID cartId);

    // 유저의 활성 장바구니가 특정 storeId인지 확인(다른 가게 담기 정책 판단용)
    Optional<Cart> findActiveCartByUserId(UUID userId);

    void fetchItemOptionsByCartId(UUID cartId);
}