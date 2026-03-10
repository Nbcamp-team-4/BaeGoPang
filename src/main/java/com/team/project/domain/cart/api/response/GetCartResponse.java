package com.team.project.domain.cart.api.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.team.project.domain.cart.entity.Cart;
import com.team.project.domain.cart.model.vo.CartStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetCartResponse {

    private UUID cartId;
    private UUID userId;
    private UUID storeId;
    private String storeName;
    private Integer deliveryFee;
    private CartStatus status;

    private Integer totalQuantity;
    private Integer itemCount;

    private List<CartItemResponse> items;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static GetCartResponse from(Cart cart) {

        int totalQty = cart.getItems().stream()
                .mapToInt(i -> i.getQuantity() == null ? 0 : i.getQuantity())
                .sum();

        return GetCartResponse.builder()
                .cartId(cart.getId())
                .userId(cart.getUser().getId())
                .storeId(cart.getStore().getId())
                .storeName(cart.getStore().getName())
                .deliveryFee(cart.getStore().getDeliveryFee())
                .status(cart.getStatus())
                .totalQuantity(totalQty)
                .itemCount(cart.getItems().size())
                .items(cart.getItems().stream()
                        .map(CartItemResponse::from)
                        .collect(Collectors.toList()))
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}