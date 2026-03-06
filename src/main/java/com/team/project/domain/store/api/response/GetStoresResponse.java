package com.team.project.domain.store.api.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetStoresResponse {
    private List<StoreResponse> stores;
}
