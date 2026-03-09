package com.team.project.domain.address.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserAddressRequest {

    @NotBlank
    @Schema(description = "배송지 이름", example = "회사")
    private String name;
    @NotBlank
    @Schema(description = "배송지 번호", example = "010-1334-1234")
    private String phone;
    @NotBlank
    @Schema(description = "배송지 주소", example = "서울시 로로구 구구로 123")
    private String address;
    @NotBlank
    @Schema(description = "배송지 상세 주소", example = "11층")
    private String detailAddress;
    @NotNull
    @Schema(description = "배송지 위도", example = "37.74913611")
    private BigDecimal latitude;
    @NotNull
    @Schema(description = "배송지 경도", example = "128.8784972")
    private BigDecimal longitude;
    @Schema(description = "기본 배송지 여부", example = "true")
    private Boolean isDefault;

}
