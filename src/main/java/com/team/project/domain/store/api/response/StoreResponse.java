package com.team.project.domain.store.api.response;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.domain.store.service.result.StoreResult;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreResponse {

    private UUID id;
    private String name;
    private String description;
    private String address;
    private Double longitude;
    private Double latitude;
    private String phone;
    private String imageUrl;
    private LocalTime openTime;
    private LocalTime closeTime;
    private StoreStatus status;
    private Integer deliveryMinMinutes;
    private Integer deliveryMaxMinutes;
    private Integer deliveryFee;
    private Integer minimumOrderAmount;
    private List<StoreProductResponse> products;

    public static StoreResponse from(StoreResult result) {
        return from(result, List.of());
    }

    public static StoreResponse from(StoreResult result, List<StoreProductResponse> products) {
        return StoreResponse.builder()
            .id(result.getId())
            .name(result.getName())
            .description(result.getDescription())
            .address(result.getAddress())
            .longitude(result.getLongitude())
            .latitude(result.getLatitude())
            .phone(result.getPhone())
            .imageUrl(result.getImageUrl())
            .openTime(result.getOpenTime())
            .closeTime(result.getCloseTime())
            .status(result.getStatus())
            .deliveryMinMinutes(result.getDeliveryMinMinutes())
            .deliveryMaxMinutes(result.getDeliveryMaxMinutes())
            .deliveryFee(result.getDeliveryFee())
            .minimumOrderAmount(result.getMinimumOrderAmount())
            .products(products)
            .build();
    }
}