package com.team.project.domain.store.api.request;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.team.project.domain.store.service.command.CreateStoreCommand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "최소 1개 이상의 카테고리를 선택해야 합니다.")
    private List<UUID> categoryIds;

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
        return CreateStoreCommand.builder()
            .userId(this.userId)      // TODO: 추후 @AuthenticationPrincipal 적용 시 제거 가능
            .regionId(this.regionId)
            .name(this.name)
            .description(this.description)
            .address(this.address)
            .longitude(this.longitude)
            .latitude(this.latitude)
            .phone(this.phone)
            .imageUrl(this.imageUrl)
            .openTime(this.openTime)
            .closeTime(this.closeTime)
            .deliveryMinMinutes(this.deliveryMinMinutes)
            .deliveryMaxMinutes(this.deliveryMaxMinutes)
            .deliveryFee(this.deliveryFee)
            .minimumOrderAmount(this.minimumOrderAmount)
            .categoryIds(this.categoryIds)
            .build();
    }
}