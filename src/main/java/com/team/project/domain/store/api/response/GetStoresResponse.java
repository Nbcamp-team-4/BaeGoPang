package com.team.project.domain.store.api.response;

import java.util.List;

import org.springframework.data.domain.Page;

import com.team.project.domain.store.service.result.StoreResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetStoresResponse {

    private List<StoreResponse> content;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;

    public static GetStoresResponse of(Page<StoreResult> pageResult) {
        List<StoreResponse> content = pageResult.getContent().stream()
            .map(StoreResponse::from)
            .toList();

        return new GetStoresResponse(
            content,
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.getTotalPages()
        );
    }

    public static GetStoresResponse of(List<StoreResult> results) {
        List<StoreResponse> content = results.stream()
            .map(StoreResponse::from)
            .toList();

        return new GetStoresResponse(
            content,
            0,
            content.size(),
            (long) content.size(),
            content.isEmpty() ? 0 : 1
        );
    }
}