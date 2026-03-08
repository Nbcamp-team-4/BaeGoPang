package com.team.project.domain.cart.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.cart.api.request.AddCartItemRequest;
import com.team.project.domain.cart.api.request.UpdateCartItemRequest;
import com.team.project.domain.cart.api.response.AddCartItemResponse;
import com.team.project.domain.cart.api.response.GetCartResponse;
import com.team.project.domain.cart.api.response.UpdateCartItemResponse;
import com.team.project.domain.cart.service.CartService;
import com.team.project.global.common.dto.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/carts")
@Slf4j
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * [고객] 장바구니 조회
     * GET /api/carts?userId={userId}
     * - 인증 적용 전 임시로 userId를 query param으로 받음
     */
    @GetMapping
    public ResponseEntity<?> getCart(@RequestParam("userId") UUID userId) {

        GetCartResponse response = cartService.getCart(userId);

        return ResponseEntity.ok().body(
                BaseResponse.ofSuccess(response)
        );
    }

    /**
     * [고객] 장바구니 상품 담기
     * POST /api/carts
     * - 다른 가게 상품일 경우 기존 장바구니 초기화 후 담기(정책은 서비스에서 처리)
     */
    @PostMapping
    public ResponseEntity<?> addCartItem(@RequestBody @Valid AddCartItemRequest request) {

        AddCartItemResponse response = cartService.addCartItem(request);

        return ResponseEntity.ok().body(
                BaseResponse.ofSuccess(response)
        );
    }

    /**
     * [고객] 장바구니 상품 수량/옵션 수정
     * PUT /api/carts/{cartId}/items/{itemId}
     * - 인증 적용 전 임시로 userId를 body로 받음
     */
    @PutMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable("cartId") UUID cartId,
                                            @PathVariable("itemId") UUID itemId,
                                            @RequestBody @Valid UpdateCartItemRequest request) {

        UpdateCartItemResponse response = cartService.updateCartItem(cartId, itemId, request);

        return ResponseEntity.ok().body(
                BaseResponse.ofSuccess(response)
        );
    }

    /**
     * [고객] 장바구니 상품 삭제
     * DELETE /api/carts/items/{itemId}?userId={userId}
     * - 인증 적용 전 임시로 userId를 query param으로 받음
     */
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("itemId") UUID itemId,
                                            @RequestParam("userId") UUID userId) {

        cartService.deleteCartItem(itemId, userId);

        return ResponseEntity.ok().body(
                BaseResponse.ofSuccess(null)
        );
    }

    /**
     * [고객] 장바구니 전체 비우기
     * DELETE /api/carts/items?userId={userId}
     * - 인증 적용 전 임시로 userId를 query param으로 받음
     */
    @DeleteMapping("/items")
    public ResponseEntity<?> clearCart(@RequestParam("userId") UUID userId) {

        cartService.clearCart(userId);

        return ResponseEntity.ok().body(
                BaseResponse.ofSuccess(null)
        );
    }
}