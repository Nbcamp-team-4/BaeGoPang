package com._team._project.domain.store.api.response;

import java.time.LocalTime;
import java.util.UUID;

import com._team._project.domain.store.model.vo.StoreStatus;
import com._team._project.domain.store.service.result.StoreResult;

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

    public static StoreResponse from(StoreResult result) {
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
            .build();
    }
}