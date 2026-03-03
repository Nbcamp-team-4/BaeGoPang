package com._team._project.domain.store.service;

import java.util.List;
import java.util.UUID;

import com._team._project.domain.store.api.request.CreateStoreRequest;
import com._team._project.domain.store.api.request.UpdateStoreRequest;
import com._team._project.domain.store.api.response.StoreResponse;

public interface StoreService {


        StoreResponse createStore(CreateStoreRequest request);

        StoreResponse updateStore(UUID storeId, UpdateStoreRequest request);

        void deleteStore(UUID storeId, UUID userId);

        List<StoreResponse> getStores();

        StoreResponse getStore(UUID storeId);
    }
    // Search(Page/Slice) + 정렬 + size 제한 필요함 (2차)
    // 지역 포함 검증/3km 반경 검색 필요함 (2차)
