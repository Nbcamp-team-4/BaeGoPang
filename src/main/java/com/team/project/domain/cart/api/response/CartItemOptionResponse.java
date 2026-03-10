package com.team.project.domain.cart.api.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CartItemOptionResponse {
    private UUID id;
    private UUID productOptionId;
    private UUID productOptionItemId;
    private String optionName;
    private String optionItemName;
    private Integer additionalPrice;

    public static CartItemOptionResponse from(com.team.project.domain.cart.entity.CartItemOption opt) {
        return CartItemOptionResponse.builder()
                .id(opt.getId())
                .productOptionId(opt.getProductOption().getId())
                .productOptionItemId(opt.getProductOptionItem().getId())
                .optionName(opt.getProductOption().getName())
                .optionItemName(opt.getProductOptionItem().getName())
                .additionalPrice(opt.getProductOptionItem().getAdditionalPrice())
                .build();
    }
}

