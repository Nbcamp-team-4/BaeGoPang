package com.team.project.domain.store.service.command;

import java.util.UUID;

import com.team.project.domain.store.model.vo.StoreStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchStoreCommand {
	private String name;        // 추가 검색 조건
	private StoreStatus status; // 인터페이스에 있던 것
	private UUID regionId;      // 인터페이스에 있던 것
	private UUID userId;        // 인터페이스에 있던 것 (점주 필터 등)
	private UUID categoryId;    // 카테고리 필터

	// 페이징 정보까지 한 번에!
	private Integer page;
	private Integer size;
}