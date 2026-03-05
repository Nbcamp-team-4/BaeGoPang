package com._team._project.domain.store.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateStoreResponse {
    private StoreResponse store;
}
