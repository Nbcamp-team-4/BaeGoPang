package com.team.project.domain.address.api.response;

import com.team.project.domain.address.entity.UserAddress;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class UserAddressResponse {

    private UUID id;
    private String addressName;
    private String phone;
    private String address;
    private String detailAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean isDefault;

    public static UserAddressResponse from(UserAddress userAddress) {
        return UserAddressResponse.builder()
                .id(userAddress.getId())
                .addressName(userAddress.getAddressName())
                .phone(userAddress.getPhone())
                .address(userAddress.getAddress())
                .detailAddress(userAddress.getDetailAddress())
                .latitude(userAddress.getLatitude())
                .longitude(userAddress.getLongitude())
                .isDefault(userAddress.getIsDefault())
                .build();
    }
}
