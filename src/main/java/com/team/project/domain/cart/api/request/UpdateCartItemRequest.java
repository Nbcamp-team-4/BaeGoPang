package com.team.project.domain.cart.api.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {

    @NotNull
    private UUID userId;

    @NotNull
    @Min(1)
    private Integer quantity;

    // 옵션 변경이 필요하면 통째로 갈아끼우는 방식(가장 단순)
    private List<CartItemOptionRequest> options;

    @Data
    public static class CartItemOptionRequest {
        @NotNull
        private UUID productOptionId;

        @NotNull
        private UUID productOptionItemId;
    }
}