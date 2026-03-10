package com.team.project.domain.product.api.request;

import java.util.UUID;

import com.team.project.global.common.dto.BasePageRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchRequest extends BasePageRequest {

	@Schema(description = "가게 ID", example = "42505e2a-4308-4574-8b9a-0b134f65691f")
	private UUID storeId;

	@Schema(description = "상품명 검색어", example = "치킨")
	@Size(max = 50)
	private String keyword;
}