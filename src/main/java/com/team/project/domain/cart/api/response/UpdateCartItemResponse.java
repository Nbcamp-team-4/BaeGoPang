package com.team.project.domain.cart.api.response;

import java.util.UUID;

import com.team.project.domain.cart.entity.CartItem;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateCartItemResponse {

    private UUID itemId;
    private UUID productId;
    private Integer quantity;

    public static UpdateCartItemResponse from(CartItem item) {
        return UpdateCartItemResponse.builder()
                .itemId(item.getId())
                .productId(item.getProduct().getId())
                .quantity(item.getQuantity())
                .build();
    }
}