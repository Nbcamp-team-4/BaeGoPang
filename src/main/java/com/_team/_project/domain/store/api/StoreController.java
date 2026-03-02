package com._team._project.domain.store.api;

import com._team._project.domain.store.api.request.CreateStoreRequest;
import com._team._project.domain.store.api.request.UpdateStoreRequest;
import com._team._project.domain.store.api.response.*;
import com._team._project.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @PreAuthorize("hasAnyRole('MASTER','MANAGER','OWNER')")
    @PostMapping
    public CreateStoreResponse createStore(@RequestBody CreateStoreRequest request) {
        return storeService.createStore(request);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{storeId}")
    public GetStoreResponse getStore(@PathVariable UUID storeId) {
        return storeService.getStore(storeId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public GetStoresResponse getStores() {
        return storeService.getStores();
    }

    @PreAuthorize("hasAnyRole('MASTER','MANAGER','OWNER')")
    @PatchMapping("/{storeId}")
    public UpdateStoreResponse updateStore(@PathVariable UUID storeId,
                                           @RequestBody UpdateStoreRequest request) {
        return storeService.updateStore(storeId, request);
    }

    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    @DeleteMapping("/{storeId}")
    public DeleteStoreResponse deleteStore(@PathVariable UUID storeId) {
        return storeService.deleteStore(storeId);
    }
}
