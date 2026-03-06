package com.team.project.domain.store.service;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.region.entity.Region;
import com.team.project.domain.region.repository.RegionRepository;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.repository.StoreRepository;
import com.team.project.domain.store.service.command.CreateStoreCommand;
import com.team.project.domain.store.service.result.StoreResult;
import com.team.project.domain.store.util.GeometryUtil;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	private final RegionRepository regionRepository;

	/*
	 * 가게 생성
	 */
	@Override
	@Transactional
	public StoreResult createStore(CreateStoreCommand command) {

		// 1) 좌표 생성
		Point location = GeometryUtil.createPoint(command.getLongitude(), command.getLatitude());
		location.setSRID(4326);


		// 2. 유저, 지역 확인
		User user = userRepository.findById(command.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

		// 3. 해당 좌표를 포함하고 있는 활성 지역(Region)을 DB에서 조회
		// 기존에 location(Point)을 넘기던 부분을 위도/경도 숫자로 변경
		Region region = regionRepository.findActiveRegionContaining(command.getLongitude(), command.getLatitude())
			.orElseThrow(() -> new IllegalArgumentException("AREA_NOT_SUPPORTED"));

		// 4. 엔티티 생성 및 저장
		Store store = new Store(
			user,
			region, // DB에서 찾은 검증된 region 객체 사용
			command.getName(),
			command.getDescription(),
			command.getAddress(),
			location,
			command.getPhone(),
			command.getImageUrl(),
			command.getOpenTime(),
			command.getCloseTime(),
			command.getDeliveryMinMinutes(),
			command.getDeliveryMaxMinutes(),
			command.getDeliveryFee(),
			command.getMinimumOrderAmount()
		);

		return StoreResult.from(storeRepository.save(store));
	}


	/*
	 * 가게 전체 조회

	@Override
	public List<StoreResponse> getStores() {

		// 1) 전체 조회
		List<Store> stores = storeRepository.findAll();

		// 2) 응답 변환
		return stores.stream()
			.map(StoreResponse::from)
			.collect(Collectors.toList());
	}



	 //가게 상세 조회

	@Override
	public StoreResponse getStore(UUID storeId) {

		// 1) 매장 조회
		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		// 2) 응답 변환
		return StoreResponse.from(store);
	}


	//가게 수정

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



	//가게 삭제

	@Override
	@Transactional
	public void deleteStore(UUID storeId, UUID userId) {

		// 1) 매장 조회
		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		// 2) Soft Delete
		store.delete(userId);
	}
	 */

}