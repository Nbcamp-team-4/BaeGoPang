package com.team.project.domain.store.service.command;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.team.project.domain.store.model.vo.StoreStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchStoreCommand {

	private UUID userId;
	private String keyword;
	private StoreStatus status;
	private UUID regionId;
	private UUID categoryId;
	private Integer page;
	private Integer size;

	public Pageable toPageable() {
		return PageRequest.of(
			page == null || page < 0 ? 0 : page,
			size == null ? 10 : size,
			Sort.by(Sort.Direction.DESC, "createdAt")
		);
	}
}