package com.team.project.domain.user.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.user.entity.UserAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUserAddressQuery {
	private UUID id;
	private String addressName;
	private String phone;
	private String address;
	private String detailAddress;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private Boolean isDefault;

	private UUID userId;
	private String userLoginId;

	public static CreateUserAddressQuery from(UserAddress saved, UserDto userDto) {
		return new CreateUserAddressQuery(saved.getId(), saved.getAddressName(), saved.getPhone(), saved.getAddress(),
			saved.getDetailAddress(), saved.getLatitude(), saved.getLongitude(), saved.getIsDefault(), userDto.getId(),
			userDto.getLoginId());
	}
}
