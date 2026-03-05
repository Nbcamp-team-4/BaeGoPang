package com._team._project.domain.store.api.request;

import java.time.LocalTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateStoreRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID regionId;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String address;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private String phone;
    private String imageUrl;

    private LocalTime openTime;
    private LocalTime closeTime;

    @NotNull
    private Integer deliveryMinMinutes;

    @NotNull
    private Integer deliveryMaxMinutes;

    private Integer deliveryFee;
    private Integer minimumOrderAmount;
}