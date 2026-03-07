package com.team.project.domain.store.api.request;

import java.util.UUID;

import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.domain.store.service.command.SearchStoreCommand;
import com.team.project.global.common.dto.BasePageRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetStoresRequest extends BasePageRequest {
	private String name;        // 가게 이름 검색
	private StoreStatus status; // 영업 상태 필터
	private UUID regionId;      // 지역별 필터
	private UUID categoryId;    // 카테고리별 필터

	public GetStoresRequest(Integer page, Integer size, String name,
		StoreStatus status, UUID regionId, UUID categoryId) {
		super(page, size);
		this.name = name;
		this.status = status;
		this.regionId = regionId;
		this.categoryId = categoryId;
	}
	// 서비스로 넘길 쿼리 객체로 변환
	public SearchStoreCommand toCommand(UUID userId) { // 인증된 유저 ID를 받아서 처리
		return SearchStoreCommand.builder()
			.name(this.name)
			.status(this.status)
			.regionId(this.regionId)
			.categoryId(this.categoryId)
			.userId(userId) // <- 여기서 userId 주입!
			.page(this.getPage())
			.size(this.getSize())
			.build();
	}
}