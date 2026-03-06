package com._team._project.domain.store.service.result;

import java.time.LocalTime;
import java.util.UUID;

import com._team._project.domain.store.entity.Store;
import com._team._project.domain.store.model.vo.StoreStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부에서 생성자 직접 호출 방지
public class StoreResult {

	private final UUID id;
	private final UUID userId;
	private final UUID regionId;
	private final String name;
	private final String description;
	private final String address;

	// Point 객체를 좌표 값으로 변환하여 전달
	private final Double longitude;
	private final Double latitude;

	private final String phone;
	private final String imageUrl;
	private final LocalTime openTime;
	private final LocalTime closeTime;
	private final StoreStatus status;

	private final Integer deliveryMinMinutes;
	private final Integer deliveryMaxMinutes;
	private final Integer deliveryFee;
	private final Integer minimumOrderAmount;

	/*
	 * Entity -> Result 변환 static factory method
	 */
	public static StoreResult from(Store store) {
		return new StoreResult(
			store.getId(),
			store.getUser().getId(),
			store.getRegion().getId(),
			store.getName(),
			store.getDescription(),
			store.getAddress(),
			store.getLocation().getX(), // 경도 (Longtitude)
			store.getLocation().getY(), // 위도 (Latitude)
			store.getPhone(),
			store.getImageUrl(),
			store.getOpenTime(),
			store.getCloseTime(),
			store.getStatus(),
			store.getDeliveryMinMinutes(),
			store.getDeliveryMaxMinutes(),
			store.getDeliveryFee(),
			store.getMinimumOrderAmount()
		);
	}
}
