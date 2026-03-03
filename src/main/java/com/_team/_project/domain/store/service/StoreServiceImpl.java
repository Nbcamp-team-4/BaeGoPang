package com._team._project.domain.store.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._team._project.domain.store.api.request.CreateStoreRequest;
import com._team._project.domain.store.api.request.UpdateStoreRequest;
import com._team._project.domain.store.api.response.StoreResponse;
import com._team._project.domain.store.entity.Store;
import com._team._project.domain.store.exception.StoreNotFoundException;
import com._team._project.domain.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    //private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Override
    public StoreResponse createStore(CreateStoreRequest request) {

        /*Point point = geometryFactory.createPoint(
            new Coordinate(request.getLongitude(), request.getLatitude())
        );
        point.setSRID(4326);
        */
        Store store = new Store(
            request.getUserId(),
            request.getRegionId(),
            request.getName(),
            request.getDescription(),
            request.getAddress(),
            //point,
            request.getPhone(),
            request.getImageUrl(),
            request.getOpenTime(),
            request.getCloseTime(),
            request.getDeliveryMinMinutes(),
            request.getDeliveryMaxMinutes(),
            request.getDeliveryFee(),
            request.getMinimumOrderAmount()
        );

        storeRepository.save(store);

        return toResponse(store);
    }

    @Override
    public StoreResponse updateStore(UUID storeId, UpdateStoreRequest request) {

        Store store = storeRepository.findById(storeId)
            .orElseThrow(StoreNotFoundException::new);

       /* Point point = null;
        if (request.getLatitude() != null && request.getLongitude() != null) {
            point = geometryFactory.createPoint(
                new Coordinate(request.getLongitude(), request.getLatitude())
            );
            point.setSRID(4326);
        }
        */
        store.update(
            request.getName(),
            request.getDescription(),
            request.getAddress(),
            //point,
            request.getPhone(),
            request.getImageUrl(),
            request.getOpenTime(),
            request.getCloseTime(),
            request.getStatus(),
            request.getDeliveryMinMinutes(),
            request.getDeliveryMaxMinutes(),
            request.getDeliveryFee(),
            request.getMinimumOrderAmount(),
            request.getUserId()
        );

        return toResponse(store);
    }

    @Override
    public void deleteStore(UUID storeId, UUID userId) {

        Store store = storeRepository.findById(storeId)
            .orElseThrow(StoreNotFoundException::new);

        store.delete(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreResponse> getStores() {

        return storeRepository.findAllByDeletedAtIsNull()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StoreResponse getStore(UUID storeId) {

        Store store = storeRepository.findById(storeId)
            .orElseThrow(StoreNotFoundException::new);

        if (store.getDeletedAt() != null) {
            throw new StoreNotFoundException();
        }

        return toResponse(store);
    }

    private StoreResponse toResponse(Store store) {

        return new StoreResponse(
            store.getId(),
            store.getUserId(),
            store.getRegionId(),
            store.getName(),
            store.getDescription(),
            store.getAddress(),
           // store.getLocation().getY(),
           // store.getLocation().getX(),
            store.getPhone(),
            store.getImageUrl(),
            store.getOpenTime(),
            store.getCloseTime(),
            store.getStatus(),
            store.getDeliveryMinMinutes(),
            store.getDeliveryMaxMinutes(),
            store.getDeliveryFee(),
            store.getMinimumOrderAmount(),
            store.getCreatedAt()
        );
    }
}