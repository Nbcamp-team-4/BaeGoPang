package com._team._project.domain.store.service;

import com._team._project.domain.store.api.request.CreateStoreRequest;
import com._team._project.domain.store.api.request.UpdateStoreRequest;
import com._team._project.domain.store.api.response.*;

import java.util.UUID;

public interface StoreService {

    CreateStoreResponse createStore(CreateStoreRequest request);

    GetStoreResponse getStore(UUID storeId);

    GetStoresResponse getStores();

    UpdateStoreResponse updateStore(UUID storeId, UpdateStoreRequest request);

    DeleteStoreResponse deleteStore(UUID storeId); // Soft Delete

    // Search(Page/Slice) + 정렬 + size 제한 필요함 (2차)
    // 지역 포함 검증/3km 반경 검색 필요함 (2차)
}
