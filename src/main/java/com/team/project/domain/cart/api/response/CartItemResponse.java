package com.team.project.domain.cart.api.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class CartItemResponse {
    private UUID itemId;
    private UUID productId;
    private String productName;
    private Integer productPrice;
    private Integer quantity;
    private List<CartItemOptionResponse> options;

    public static CartItemResponse from(com.team.project.domain.cart.entity.CartItem item) {
        return CartItemResponse.builder()
                .itemId(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productPrice(item.getProduct().getPrice())
                .quantity(item.getQuantity())
                .options(item.getOptions().stream()
                        .map(CartItemOptionResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}