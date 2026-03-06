package com._team._project.domain.store.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com._team._project.domain.store.api.request.CreateStoreRequest;
import com._team._project.domain.store.api.response.StoreResponse;
import com._team._project.domain.store.service.StoreService;
import com._team._project.domain.store.service.result.StoreResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    //가게 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreResponse createStore(@RequestBody @Valid CreateStoreRequest request) {

        // 1. Request를 Command로 변환 (userId 포함)
        // 나중에 Security가 도입되면 request.toCommand(currentUserId)로 변경
        StoreResult result = storeService.createStore(request.toCommand());

        // 2. Result를 Response로 변환하여 반환
        return StoreResponse.from(result);
    }


    /*가게 전체 조회
    @GetMapping
    public List<StoreResponse> getStores() {
        return storeService.getStores();
    }


    //가게 상세 조회
    @GetMapping("/{storeId}")
    public StoreResponse getStore(@PathVariable UUID storeId) {
        return storeService.getStore(storeId);
    }


    //가게 수정
    @PutMapping("/{storeId}")
    public StoreResponse updateStore(
        @PathVariable UUID storeId,
        @RequestBody UpdateStoreRequest request
    ) {
        return storeService.updateStore(storeId, request);
    }


    //가게 삭제 (Soft Delete)
    @DeleteMapping("/{storeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStore(
        @PathVariable UUID storeId,
        @RequestParam UUID userId
    ) {
        storeService.deleteStore(storeId, userId);
    }
    */

}