package com.team.project.domain.address.api.request;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateUserAddressRequest {
	@NotBlank
	@Schema(description = "배송지 이름", example = "집")
	private String name;
	@NotBlank
	@Schema(description = "배송지 번호", example = "010-1234-1234")
	private String phone;
	@NotBlank
	@Schema(description = "배송지 주소", example = "서울시 구구구 로로로 123")
	private String address;
	@NotBlank
	@Schema(description = "배송지 상세 주소", example = "1층")
	private String detailAddress;
	@NotNull
	@Schema(description = "배송지 위도", example = "37.74913611")
	private BigDecimal latitude;
	@NotNull
	@Schema(description = "배송지 경도", example = "128.8784972")
	private BigDecimal longitude;
	@Schema(description = "기본 배송지 여부", example = "false")
	private Boolean isDefault = false;

}
