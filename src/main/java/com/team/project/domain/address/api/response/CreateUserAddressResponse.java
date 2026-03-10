package com.team.project.domain.address.api.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.team.project.domain.address.dto.CreateUserAddressQuery;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUserAddressResponse {
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

	public static CreateUserAddressResponse from(CreateUserAddressQuery query) {
		return new CreateUserAddressResponse(query.getId(), query.getAddressName(), query.getPhone(),
			query.getAddress(), query.getDetailAddress(), query.getLatitude(), query.getLongitude(),
			query.getIsDefault(), query.getUserId(), query.getUserLoginId());
	}
}
