package com.team.project.domain.store.api.request;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.domain.store.service.command.UpdateStoreByAdminCommand;

import lombok.Getter;

@Getter
public class AdminUpdateStoreRequest {

	private String name;
	private String description;
	private String address;
	private Double latitude;
	private Double longitude;
	private String phone;
	private String imageUrl;
	private LocalTime openTime;
	private LocalTime closeTime;
	private Integer deliveryMinMinutes;
	private Integer deliveryMaxMinutes;
	private Integer deliveryFee;
	private Integer minimumOrderAmount;
	private StoreStatus status;
	private UUID regionId;
	private List<UUID> categoryIds;

	public UpdateStoreByAdminCommand toCommand(UUID storeId, UUID userId) {
		return UpdateStoreByAdminCommand.builder()
			.storeId(storeId)
			.userId(userId)
			.name(this.name)
			.description(this.description)
			.address(this.address)
			.latitude(this.latitude)
			.longitude(this.longitude)
			.phone(this.phone)
			.imageUrl(this.imageUrl)
			.openTime(this.openTime)
			.closeTime(this.closeTime)
			.deliveryMinMinutes(this.deliveryMinMinutes)
			.deliveryMaxMinutes(this.deliveryMaxMinutes)
			.deliveryFee(this.deliveryFee)
			.minimumOrderAmount(this.minimumOrderAmount)
			.status(this.status)
			.regionId(this.regionId)
			.categoryIds(this.categoryIds)
			.build();
	}
}