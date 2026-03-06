package com.team.project.domain.store.service.command;

import java.time.LocalTime;
import java.util.UUID;

import com.team.project.domain.store.model.vo.StoreStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateStoreByAdminCommand {
	private final UUID storeId;
	private final UUID regionId;
	//private final UUID categoryId;
	private final String name;
	private final String description;
	private final String address;
	private final Double longitude;
	private final Double latitude;
	private final String phone;
	private final String imageUrl;
	private final LocalTime openTime;
	private final LocalTime closeTime;
	private final Integer deliveryMinMinutes;
	private final Integer deliveryMaxMinutes;
	private final Integer deliveryFee;
	private final Integer minimumOrderAmount;
	private final StoreStatus status;         // OPEN, CLOSED, PENDING, BANNED 등 상태 제어
}