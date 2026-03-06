package com.team.project.domain.store.api.request;

import java.time.LocalTime;
import java.util.UUID;

import com.team.project.domain.store.model.vo.StoreStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class UpdateStoreRequest {

    @NotNull
    private UUID userId; // 수정자

    private String name;
    private String description;
    private String address;

    private Double latitude;
    private Double longitude;

    private String phone;
    private String imageUrl;

    private LocalTime openTime;
    private LocalTime closeTime;

    private StoreStatus status;

    private Integer deliveryMinMinutes;
    private Integer deliveryMaxMinutes;

    private Integer deliveryFee;
    private Integer minimumOrderAmount;
}