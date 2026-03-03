package com._team._project.domain.store.api.response;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com._team._project.domain.store.entity.StoreStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class StoreResponse {

    private UUID id;
    private UUID userId;
    private UUID regionId;

    private String name;
    private String description;
    private String address;

    //private double latitude;
    //private double longitude;

    private String phone;
    private String imageUrl;

    private LocalTime openTime;
    private LocalTime closeTime;

    private StoreStatus status;

    private Integer deliveryMinMinutes;
    private Integer deliveryMaxMinutes;
    private Integer deliveryFee;
    private Integer minimumOrderAmount;

    private LocalDateTime createdAt;
}
