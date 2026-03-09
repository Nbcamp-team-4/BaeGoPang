package com.team.project.domain.cart.api.response;

import java.util.UUID;

import com.team.project.domain.cart.entity.Cart;
import com.team.project.domain.cart.model.vo.CartStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddCartItemResponse {

    private UUID cartId;
    private CartStatus status;

    private UUID addedItemId;
    private UUID productId;
    private Integer quantity;

    // 다른 가게 상품 담아서 장바구니가 초기화됐는지 여부
    private boolean cartReset;

    public static AddCartItemResponse of(Cart cart, UUID addedItemId, UUID productId, Integer quantity, boolean cartReset) {
        return AddCartItemResponse.builder()
                .cartId(cart.getId())
                .status(cart.getStatus())
                .addedItemId(addedItemId)
                .productId(productId)
                .quantity(quantity)
                .cartReset(cartReset)
                .build();
    }
}