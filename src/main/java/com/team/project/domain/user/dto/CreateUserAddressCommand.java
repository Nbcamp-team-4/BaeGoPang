package com.team.project.domain.user.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUserAddressCommand {
	private String name;
	private String phone;
	private String address;
	private String detailAddress;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private Boolean isDefault;

	public static CreateUserAddressCommand of(String name, String phone, String address, String detailAddress,
		BigDecimal latitude, BigDecimal longitude, Boolean isDefault) {
		return new CreateUserAddressCommand(name, phone, address, detailAddress, latitude, longitude, isDefault);
	}

}
