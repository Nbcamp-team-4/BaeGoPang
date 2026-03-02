package com._team._project.domain.store.service;

import com._team._project.domain.store.api.request.CreateStoreRequest;
import com._team._project.domain.store.api.request.UpdateStoreRequest;
import com._team._project.domain.store.api.response.*;
import com._team._project.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;


    @Override
    public CreateStoreResponse createStore(CreateStoreRequest request) {
        return null;
    }

    @Override
    public GetStoreResponse getStore(UUID storeId) {
        return null;
    }

    @Override
    public GetStoresResponse getStores() {
        return null;
    }

    @Override
    public UpdateStoreResponse updateStore(UUID storeId, UpdateStoreRequest request) {
        return null;
    }

    @Override
    public DeleteStoreResponse deleteStore(UUID storeId) {
        return null;
    }
}
