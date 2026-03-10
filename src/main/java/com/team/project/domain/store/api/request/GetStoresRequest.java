package com.team.project.domain.store.api.request;

import java.util.UUID;

import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.domain.store.service.command.SearchStoreCommand;
import com.team.project.global.common.dto.BasePageRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetStoresRequest extends BasePageRequest {

	@Schema(description = "가게명 검색어")
	private String keyword;

	@Schema(description = "가게 상태")
	private StoreStatus status;

	@Schema(description = "지역 ID")
	private UUID regionId;

	@Schema(description = "카테고리 ID")
	private UUID categoryId;


	public SearchStoreCommand toCommand(UUID userId) {
		return SearchStoreCommand.builder()
			.userId(userId)
			.keyword(keyword)
			.status(status)
			.regionId(regionId)
			.categoryId(categoryId)
			.page(getPage())
			.size(getSize())
			.build();
	}
}