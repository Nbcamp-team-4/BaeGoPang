package com._team._project.domain.store.service;

import java.util.List;
import java.util.UUID;

import com._team._project.domain.store.api.request.CreateStoreRequest;
import com._team._project.domain.store.api.request.UpdateStoreRequest;
import com._team._project.domain.store.api.response.StoreResponse;

public interface StoreService {

       //가게 생성
        StoreResponse createStore(CreateStoreRequest request);

        //가게수정
        StoreResponse updateStore(UUID storeId, UpdateStoreRequest request);

        //가게 삭제 (Soft Delete)
        void deleteStore(UUID storeId, UUID userId);

        //가게 전체 조회
        List<StoreResponse> getStores();

        //가게 단건 조회
        StoreResponse getStore(UUID storeId);

}

/*
 * 2차 구현 예정
 *
 * - Search(Page / Slice)
 * - 정렬 + size 제한
 * - 지역 포함 검증
 * - 3km 반경 매장 검색 (PostGIS)
 */
