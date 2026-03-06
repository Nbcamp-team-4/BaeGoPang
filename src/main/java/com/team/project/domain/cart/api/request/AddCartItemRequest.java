package com.team.project.domain.cart.api.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCartItemRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID storeId;

    @NotNull
    private UUID productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    // 옵션이 없다면 빈 리스트 또는 null 가능
    private List<CartItemOptionRequest> options;

    @Data
    public static class CartItemOptionRequest {
        @NotNull
        private UUID productOptionId;

        @NotNull
        private UUID productOptionItemId;
    }
}