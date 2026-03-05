package com._team._project.domain.store.api.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetStoresResponse {
    private List<StoreResponse> stores;
}
