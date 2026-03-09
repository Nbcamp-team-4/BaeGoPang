package com.team.project.domain.address.api.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateUserAddressRequest {
	@NotBlank
	private String name;
	@NotBlank
	private String phone;
	@NotBlank
	private String address;
	@NotBlank
	private String detailAddress;
	@NotNull
	private BigDecimal latitude;
	@NotNull
	private BigDecimal longitude;
	private Boolean isDefault = false;
}
