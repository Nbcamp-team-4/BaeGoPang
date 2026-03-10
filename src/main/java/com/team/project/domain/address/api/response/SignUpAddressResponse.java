package com.team.project.domain.address.api.response;

import com.team.project.domain.address.dto.CreateUserAddressQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SignUpAddressResponse {

    @Schema(description = "배송지 이름", example = "집")
    private String addressName;

    @Schema(description = "배송지 번호", example = "010-1111-1111")
    private String addressPhone;

    @Schema(description = "배송지 주소", example = "서울시 강남구 테헤란로 123")
    private String address;

    @Schema(description = "배송지 상세 주소", example = "101동 1001호")
    private String detailAddress;

    @Schema(description = "배송지 위도", example = "37.74913611")
    private BigDecimal latitude;

    @Schema(description = "배송지 경도", example = "128.8784972")
    private BigDecimal longitude;

    @Schema(description = "기본 배송지 여부", example = "true")
    private Boolean isDefault;

    public static SignUpAddressResponse from(CreateUserAddressQuery query) {
        return SignUpAddressResponse.builder()
                .addressName(query.getAddressName())
                .addressPhone(query.getPhone())
                .address(query.getAddress())
                .detailAddress(query.getDetailAddress())
                .latitude(query.getLatitude())
                .longitude(query.getLongitude())
                .isDefault(query.getIsDefault())
                .build();
    }
}