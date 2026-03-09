package com.team.project.domain.order.api.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.team.project.domain.order.model.dto.GetOrdersQuery;
import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.global.common.dto.BasePageResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "주문 목록 조회 응답")
public class GetOrdersResponse extends BasePageResponse<GetOrdersResponse.Item> {

    public GetOrdersResponse(
            List<Item> content,
            Integer page,
            Integer size,
            Long totalElements,
            Integer totalPages
    ) {
        super(content, page, size, totalElements, totalPages);
    }

    public static GetOrdersResponse from(GetOrdersQuery query) {
        List<Item> items = query.getContent()
                .stream()
                .map(item -> Item.builder()
                        .id(item.getId())
                        .orderNo(item.getOrderNo())
                        .status(item.getStatus())
                        .totalAmount(item.getTotalAmount())
                        .createdAt(item.getCreatedAt())
                        .storeId(item.getStoreId())
                        .storeName(item.getStoreName())
                        .userId(item.getUserId())
                        .userName(item.getUserName())
                        .build())
                .toList();

        return new GetOrdersResponse(
                items,
                query.getPage(),
                query.getSize(),
                query.getTotalElements(),
                query.getTotalPages()
        );
    }

    @Getter
    @Builder
    @Schema(description = "주문 목록 항목")
    public static class Item {

        @Schema(description = "주문 ID", format = "uuid")
        private UUID id;

        @Schema(description = "주문 번호", example = "ORD-1234ABCD")
        private String orderNo;

        @Schema(description = "주문 상태", example = "PAID")
        private OrderStatus status;

        @Schema(description = "총 주문 금액", example = "18000")
        private Integer totalAmount;

        @Schema(description = "주문 생성 시각")
        private LocalDateTime createdAt;

        @Schema(description = "가게 ID", format = "uuid")
        private UUID storeId;

        @Schema(description = "가게명", example = "배고팡치킨")
        private String storeName;

        @Schema(description = "주문자 ID", format = "uuid")
        private UUID userId;

        @Schema(description = "주문자명", example = "홍길동")
        private String userName;
    }
}