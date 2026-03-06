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
    private CartStatus status;

    private Integer totalQuantity; // 총 수량(아이템 수량 합)
    private Integer itemCount;      // 아이템 라인 수

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
                .status(cart.getStatus())
                .totalQuantity(totalQty)
                .itemCount(cart.getItems().size())
                .items(cart.getItems().stream().map(CartItemResponse::from).collect(Collectors.toList()))
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    public static GetCartResponse empty() {
        // 프로젝트 응답 규격에 맞춰 "없는 장바구니"를 빈 형태로 내려줌
        // items는 반드시 빈 리스트로 내려줘야 FE에서 분기 줄어듦
        return GetCartResponse.builder()
                .cartId(null)
                .storeId(null)
                .items(java.util.List.of())
                .build();
    }

    @Getter
    @Builder
    public static class CartItemResponse {
        private UUID itemId;
        private UUID productId;
        private Integer quantity;
        private List<CartItemOptionResponse> options;

        public static CartItemResponse from(com.team.project.domain.cart.entity.CartItem item) {
            return CartItemResponse.builder()
                    .itemId(item.getId())
                    .productId(item.getProduct().getId())
                    .quantity(item.getQuantity())
                    .options(item.getOptions().stream().map(CartItemOptionResponse::from).collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CartItemOptionResponse {
        private UUID id;
        private UUID productOptionId;
        private UUID productOptionItemId;

        public static CartItemOptionResponse from(com.team.project.domain.cart.entity.CartItemOption opt) {
            return CartItemOptionResponse.builder()
                    .id(opt.getId())
                    .productOptionId(opt.getProductOption().getId())
                    .productOptionItemId(opt.getProductOptionItem().getId())
                    .build();
        }
    }
}