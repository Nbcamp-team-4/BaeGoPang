package com.team.project.domain.address.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserAddressRequest {

    @NotBlank
    private String addressName;
    @NotBlank
    private String phone;
    @NotBlank
    private String address;
    @NotBlank
    private String detailAddress;
    @NotBlank
    private BigDecimal latitude;
    @NotBlank
    private BigDecimal longitude;
    @NotBlank
    private Boolean isDefault;
}
