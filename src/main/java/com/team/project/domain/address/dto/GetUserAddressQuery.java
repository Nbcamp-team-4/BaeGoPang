package com.team.project.domain.address.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.address.entity.UserAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserAddressQuery {
	private UUID userAddressId;
	private String userAddressName;
	private String userAddressPhone;
	private String userAddressAddress;
	private BigDecimal userAddressLatitude;
	private BigDecimal userAddressLongitude;
	private Boolean userAddressIsDefault;
	private UUID userId;
	private String userLoginId;

	public static GetUserAddressQuery from(UserAddress found, UserDto userDto) {
		return new GetUserAddressQuery(found.getId(), found.getAddressName(), found.getPhone(), found.getAddress(),
			found.getLatitude(), found.getLongitude(), found.getIsDefault(), userDto.getId(), userDto.getLoginId());
	}
}
