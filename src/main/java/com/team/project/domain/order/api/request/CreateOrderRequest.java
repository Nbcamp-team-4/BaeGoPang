package com.team.project.domain.order.api.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotNull
    private UUID userId; // 인증 붙으면 SecurityContext로 대체 가능

    @NotNull
    private UUID storeId;

    private UUID deliveryAddressId;

    private String requestMemo;

    @NotEmpty
    private List<CreateOrderItemRequest> items;

    @Data
    public static class CreateOrderItemRequest {

        @NotNull
        private UUID productId;

        @jakarta.validation.constraints.NotBlank
        private String productName; // 주문 시점 스냅샷(변경 대비)

        @NotNull
        @Positive
        private Integer unitPrice;

        @NotNull
        @Positive
        private Integer quantity;

        private List<CreateOrderItemOptionRequest> options;
    }

    @Data
    public static class CreateOrderItemOptionRequest {

        @NotBlank
        private String optionName;

        @NotBlank
        private String optionItemName;

        @NotNull
        private Integer extraPrice; // 0 가능
    }
}