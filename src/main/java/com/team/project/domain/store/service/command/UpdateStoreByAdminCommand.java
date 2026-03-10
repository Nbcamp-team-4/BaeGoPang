package com.team.project.domain.store.service.command;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.team.project.domain.store.model.vo.StoreStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateStoreByAdminCommand {

	private final UUID storeId;
	private final UUID userId;
	private final String name;
	private final String description;
	private final String address;
	private final Double latitude;
	private final Double longitude;
	private final String phone;
	private final String imageUrl;
	private final LocalTime openTime;
	private final LocalTime closeTime;
	private final Integer deliveryMinMinutes;
	private final Integer deliveryMaxMinutes;
	private final Integer deliveryFee;
	private final Integer minimumOrderAmount;
	private final StoreStatus status;
	private final UUID regionId;
	private final List<UUID> categoryIds;

	@Builder
	public UpdateStoreByAdminCommand(
		UUID storeId,
		UUID userId,
		String name,
		String description,
		String address,
		Double latitude,
		Double longitude,
		String phone,
		String imageUrl,
		LocalTime openTime,
		LocalTime closeTime,
		Integer deliveryMinMinutes,
		Integer deliveryMaxMinutes,
		Integer deliveryFee,
		Integer minimumOrderAmount,
		StoreStatus status,
		UUID regionId,
		List<UUID> categoryIds
	) {
		this.storeId = storeId;
		this.userId = userId;
		this.name = name;
		this.description = description;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.phone = phone;
		this.imageUrl = imageUrl;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.deliveryMinMinutes = deliveryMinMinutes;
		this.deliveryMaxMinutes = deliveryMaxMinutes;
		this.deliveryFee = deliveryFee;
		this.minimumOrderAmount = minimumOrderAmount;
		this.status = status;
		this.regionId = regionId;
		this.categoryIds = categoryIds;
	}
}