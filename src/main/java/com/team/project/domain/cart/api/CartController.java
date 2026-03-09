package com.team.project.domain.cart.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.auth.dto.CurrentUser;
import com.team.project.domain.auth.dto.UserDto;
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
     * - 로그인한 사용자 기준으로 장바구니 조회
     */
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getCart(@CurrentUser UserDto userDto) {

        GetCartResponse response = cartService.getCart(userDto.getId());

        return ResponseEntity.ok().body(
                BaseResponse.ofSuccess(response)
        );
    }

    /**
     * [고객] 장바구니 상품 담기
     * - 로그인한 사용자 기준으로 상품 추가
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> addCartItem(
            @CurrentUser UserDto userDto,
            @RequestBody @Valid AddCartItemRequest request
    ) {

        AddCartItemResponse response = cartService.addCartItem(userDto.getId(), request);

        return ResponseEntity.ok().body(
                BaseResponse.ofSuccess(response)
        );
    }

    /**
     * [고객] 장바구니 상품 수량/옵션 수정
     * - 로그인한 사용자 본인 장바구니만 수정 가능
     */
    @PutMapping("/{cartId}/items/{itemId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updateCartItem(
            @CurrentUser UserDto userDto,
            @PathVariable("cartId") UUID cartId,
            @PathVariable("itemId") UUID itemId,
            @RequestBody @Valid UpdateCartItemRequest request
    ) {

        UpdateCartItemResponse response = cartService.updateCartItem(userDto.getId(), cartId, itemId, request);

        return ResponseEntity.ok().body(
                BaseResponse.ofSuccess(response)
        );
    }

    /**
     * [고객] 장바구니 상품 삭제
     * - 로그인한 사용자 본인 장바구니만 삭제 가능
     */
    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> deleteCartItem(
            @CurrentUser UserDto userDto,
            @PathVariable("itemId") UUID itemId
    ) {

        cartService.deleteCartItem(itemId, userDto.getId());

        return ResponseEntity.ok().body(
                BaseResponse.ofSuccess(null)
        );
    }

    /**
     * [고객] 장바구니 전체 비우기
     * - 로그인한 사용자 기준으로 전체 삭제
     */
    @DeleteMapping("/items")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> clearCart(@CurrentUser UserDto userDto) {

        cartService.clearCart(userDto.getId());

        return ResponseEntity.ok().body(
                BaseResponse.ofSuccess(null)
        );
    }
}