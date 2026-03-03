package com._team._project.domain.store.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com._team._project.domain.store.api.request.CreateStoreRequest;
import com._team._project.domain.store.api.request.UpdateStoreRequest;
import com._team._project.domain.store.api.response.StoreResponse;
import com._team._project.domain.store.service.StoreService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreResponse createStore(@Valid @RequestBody CreateStoreRequest request) {
        return storeService.createStore(request);
    }

    @PutMapping("/{storeId}")
    public StoreResponse updateStore(
        @PathVariable UUID storeId,
        @Valid @RequestBody UpdateStoreRequest request
    ) {
        return storeService.updateStore(storeId, request);
    }

    @DeleteMapping("/{storeId}")
    public void deleteStore(
        @PathVariable UUID storeId,
        @RequestParam UUID userId
    ) {
        storeService.deleteStore(storeId, userId);
    }

    @GetMapping
    public List<StoreResponse> getStores() {
        return storeService.getStores();
    }

    @GetMapping("/{storeId}")
    public StoreResponse getStore(@PathVariable UUID storeId) {
        return storeService.getStore(storeId);
    }
}