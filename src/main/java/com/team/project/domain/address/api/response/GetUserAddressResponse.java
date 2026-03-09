package com.team.project.domain.address.api.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.team.project.domain.address.dto.GetUserAddressQuery;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserAddressResponse {
	private UUID userAddressId;
	private String userAddressName;
	private String userAddressPhone;
	private String userAddressAddress;
	private BigDecimal userAddressLatitude;
	private BigDecimal userAddressLongitude;
	private Boolean userAddressIsDefault;
	private UUID userId;
	private String userLoginId;

	public static GetUserAddressResponse from(GetUserAddressQuery query) {
		return new GetUserAddressResponse(query.getUserAddressId(), query.getUserAddressName(),
			query.getUserAddressPhone(),
			query.getUserAddressAddress(), query.getUserAddressLatitude(), query.getUserAddressLongitude(),
			query.getUserAddressIsDefault(),
			query.getUserId(), query.getUserLoginId());

	}
}
