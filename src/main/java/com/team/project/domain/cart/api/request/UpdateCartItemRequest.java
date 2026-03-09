package com.team.project.domain.cart.api.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {

    // 로그인 사용자는 토큰에서 가져오므로 userId 제거
    @NotNull
    @Min(1)
    private Integer quantity;

    // 옵션 변경 시 통째로 교체
    private List<CartItemOptionRequest> options;

    @Data
    public static class CartItemOptionRequest {
        @NotNull
        private UUID productOptionId;

        @NotNull
        private UUID productOptionItemId;
    }
}