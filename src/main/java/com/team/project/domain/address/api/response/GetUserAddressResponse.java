package com.team.project.domain.address.api.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.team.project.domain.address.dto.GetUserAddressQuery;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Schema(description = "배송지 목록 조회 응답")
@Getter
@AllArgsConstructor
public class GetUserAddressResponse {
	@Schema(description = "배송지 ID", format = "uuid")
	private UUID userAddressId;
	@Schema(description = "배송지 이름", example = "집")
	private String userAddressName;
	@Schema(description = "배송지 번호", example = "010-1234-1234")
	private String userAddressPhone;
	@Schema(description = "배송지 주소", example = "서울시 구구구 로로로 123")
	private String userAddressAddress;
	@Schema(description = "배송지 위도", example = "37.74913611")
	private BigDecimal userAddressLatitude;
	@Schema(description = "배송지 경도", example = "128.8784972")
	private BigDecimal userAddressLongitude;
	@Schema(description = "기본 배송지 여부", example = "false")
	private Boolean userAddressIsDefault;
	@Schema(description = "유저 ID", example = "123")
	private UUID userId;
	@Schema(description = "Login ID", example = "test1234")
	private String userLoginId;

	public static GetUserAddressResponse from(GetUserAddressQuery query) {
		return new GetUserAddressResponse(query.getUserAddressId(), query.getUserAddressName(),
			query.getUserAddressPhone(),
			query.getUserAddressAddress(), query.getUserAddressLatitude(), query.getUserAddressLongitude(),
			query.getUserAddressIsDefault(),
			query.getUserId(), query.getUserLoginId());

	}
}
