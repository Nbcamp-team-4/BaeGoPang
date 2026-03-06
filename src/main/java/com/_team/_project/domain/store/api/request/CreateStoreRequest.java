package com._team._project.domain.store.api.request;

import java.time.LocalTime;
import java.util.UUID;

import com._team._project.domain.store.service.command.CreateStoreCommand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // JSON 파싱을 위해 필요
public class CreateStoreRequest {

    @NotNull(message = "사용자 ID는 필수입니다.")
    private UUID userId;

    @NotNull(message = "지역 ID는 필수입니다.")
    private UUID regionId;

    @NotBlank(message = "가게 이름은 필수입니다.")
    private String name;

    private String description;

    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;

    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;

    private String phone;
    private String imageUrl;

    private LocalTime openTime;
    private LocalTime closeTime;

    @NotNull(message = "최소 배달 시간은 필수입니다.")
    private Integer deliveryMinMinutes;

    @NotNull(message = "최대 배달 시간은 필수입니다.")
    private Integer deliveryMaxMinutes;

    private Integer deliveryFee;
    private Integer minimumOrderAmount;

    /**
     * Request DTO를 Service용 Command로 변환
     */
    public CreateStoreCommand toCommand() {
        return new CreateStoreCommand(
            this.userId,
            this.regionId,
            this.name,
            this.description,
            this.address,
            this.longitude,
            this.latitude,
            this.phone,
            this.imageUrl,
            this.openTime,
            this.closeTime,
            this.deliveryMinMinutes,
            this.deliveryMaxMinutes,
            this.deliveryFee,
            this.minimumOrderAmount
        );
    }
}