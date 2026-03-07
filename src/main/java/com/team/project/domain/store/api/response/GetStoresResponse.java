package com.team.project.domain.store.api.response;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.domain.store.service.result.StoreResult;
import com.team.project.global.common.dto.BasePageRequest;
import com.team.project.global.common.dto.BasePageResponse;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class GetStoresResponse extends BasePageResponse<GetStoresResponse.Item> {

    @Builder
    public GetStoresResponse(List<Item> content, Integer page, Integer size, Long totalElements, Integer totalPages) {
        super(content, page, size, totalElements, totalPages);
    }

    // [방법 A] 정적 팩토리 메서드: 복잡한 변환 로직을 이 안으로 격리!
    public static GetStoresResponse of(List<StoreResult> results, BasePageRequest pageRequest) {
        // 1. Result -> Item 변환
        List<Item> items = results.stream()
            .map(result -> Item.builder()
                .id(result.getId())
                .name(result.getName())
                .imageUrl(result.getImageUrl())
                .status(result.getStatus())
                .deliveryFee(result.getDeliveryFee())
                .minimumOrderAmount(result.getMinimumOrderAmount())
                .build())
            .toList();

        // 2. 자기 자신(Response)을 생성해서 반환
        return GetStoresResponse.builder()
            .content(items)
            .page(pageRequest.getPage())
            .size(pageRequest.getSize())
            .totalElements((long) items.size()) // 실제로는 DB count 쿼리 결과가 들어갈 자리
            .totalPages(1) // 실제로는 계산 로직이 들어갈 자리
            .build();
    }

    @Data
    @Builder
    public static class Item {
        private UUID id;
        private String name;
        private String imageUrl;
        private StoreStatus status;
        private Integer deliveryFee;
        private Integer minimumOrderAmount;
    }
}