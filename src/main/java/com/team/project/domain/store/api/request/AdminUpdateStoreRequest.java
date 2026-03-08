package com.team.project.domain.store.api.request;

import java.time.LocalTime;
import java.util.UUID;

import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.domain.store.service.command.UpdateStoreByAdminCommand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUpdateStoreRequest {
	// 관리자는 모든 필드를 수정할 수 있음
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
	private StoreStatus status; // 관리자는 운영 상태도 직접 변경 가능

	public UpdateStoreByAdminCommand toCommand(UUID storeId) {
		return UpdateStoreByAdminCommand.builder()
			.storeId(storeId)
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
			.build();
	}
}