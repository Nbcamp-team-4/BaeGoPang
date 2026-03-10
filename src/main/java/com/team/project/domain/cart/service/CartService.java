package com.team.project.domain.cart.service;

import java.util.UUID;

import com.team.project.domain.cart.api.request.AddCartItemRequest;
import com.team.project.domain.cart.api.request.UpdateCartItemRequest;
import com.team.project.domain.cart.api.response.AddCartItemResponse;
import com.team.project.domain.cart.api.response.GetCartResponse;
import com.team.project.domain.cart.api.response.UpdateCartItemResponse;

public interface CartService {

    // ======================
    // customer
    // ======================

    // 장바구니 조회 (유저의 ACTIVE 장바구니)
    GetCartResponse getCart(UUID userId);

    // 장바구니 상품 담기
    // - 다른 가게 상품이면 기존 장바구니 비우고 store 교체
    AddCartItemResponse addCartItem(UUID userId, AddCartItemRequest request);

    // 장바구니 상품 수량/옵션 수정
    UpdateCartItemResponse updateCartItem(UUID userId, UUID cartId, UUID itemId, UpdateCartItemRequest request);

    // 장바구니 상품 삭제
    void deleteCartItem(UUID itemId, UUID userId);

    // 장바구니 전체 비우기
    void clearCart(UUID userId);
}