package com.team.project.domain.store.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.store.api.request.CreateStoreRequest;
import com.team.project.domain.store.api.request.UpdateStoreRequest;
import com.team.project.domain.store.api.response.StoreResponse;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.exception.StoreNotFoundException;
import com.team.project.domain.store.repository.StoreRepository;
import com.team.project.domain.store.util.GeometryUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

	private final StoreRepository storeRepository;


	/*
	 * 가게 생성
	 */
	@Override
	@Transactional
	public StoreResponse createStore(CreateStoreRequest request) {

		// 1) 좌표 생성
		Point location = GeometryUtil.createPoint(
			request.getLongitude(),
			request.getLatitude()
		);

		// 2) 엔티티 생성
		Store store = new Store(
			request.getUserId(),
			request.getRegionId(),
			request.getName(),
			request.getDescription(),
			request.getAddress(),
			location,
			request.getPhone(),
			request.getImageUrl(),
			request.getOpenTime(),
			request.getCloseTime(),
			request.getDeliveryMinMinutes(),
			request.getDeliveryMaxMinutes(),
			request.getDeliveryFee(),
			request.getMinimumOrderAmount()
		);

		// 3) 저장
		Store savedStore = storeRepository.save(store);

		// 4) 응답 변환
		return StoreResponse.from(savedStore);
	}


	/*
	 * 가게 전체 조회
	 */
	@Override
	public List<StoreResponse> getStores() {

		// 1) 전체 조회
		List<Store> stores = storeRepository.findAll();

		// 2) 응답 변환
		return stores.stream()
			.map(StoreResponse::from)
			.collect(Collectors.toList());
	}


	/*
	 * 가게 상세 조회
	 */
	@Override
	public StoreResponse getStore(UUID storeId) {

		// 1) 매장 조회
		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		// 2) 응답 변환
		return StoreResponse.from(store);
	}


	/*
	 * 가게 수정
	 */
	@Override
	@Transactional
	public StoreResponse updateStore(UUID storeId, UpdateStoreRequest request) {

		// 1) 매장 조회
		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		// 2) 매장 수정
		store.update(
			request.getName(),
			request.getDescription(),
			request.getAddress(),
			request.getPhone(),
			request.getImageUrl(),
			request.getOpenTime(),
			request.getCloseTime(),
			request.getStatus(),
			request.getDeliveryMinMinutes(),
			request.getDeliveryMaxMinutes(),
			request.getDeliveryFee(),
			request.getMinimumOrderAmount()
		);

		// 3) 응답 반환
		return StoreResponse.from(store);
	}


	/*
	 * 가게 삭제
	 */
	@Override
	@Transactional
	public void deleteStore(UUID storeId, UUID userId) {

		// 1) 매장 조회
		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		// 2) Soft Delete
		store.delete(userId);
	}

}