package com.team.project.domain.cart.api.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCartItemRequest {

    // 로그인 사용자는 토큰에서 가져오므로 userId 제거
    @NotNull
    private UUID storeId;

    @NotNull
    private UUID productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    // 옵션이 없다면 null 또는 빈 리스트 허용
    private List<CartItemOptionRequest> options;

    @Data
    public static class CartItemOptionRequest {
        @NotNull
        private UUID productOptionId;

        @NotNull
        private UUID productOptionItemId;
    }
}