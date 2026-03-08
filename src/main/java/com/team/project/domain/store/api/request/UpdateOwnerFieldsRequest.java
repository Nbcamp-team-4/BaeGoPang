package com.team.project.domain.store.api.request;

import java.time.LocalTime;
import java.util.UUID;

import com.team.project.domain.store.service.command.UpdateOwnerFieldsCommand;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateOwnerFieldsRequest {
	private String description;
	private String phone;
	private String imageUrl;
	private LocalTime openTime;
	private LocalTime closeTime;
	private Integer deliveryMinMinutes;
	private Integer deliveryMaxMinutes;
	private Integer deliveryFee;
	private Integer minimumOrderAmount;

	// Command로 변환하는 메서드 (서비스에 전달용)
	public UpdateOwnerFieldsCommand toCommand(UUID userId) {
		return UpdateOwnerFieldsCommand.builder()
			.userId(userId)
			.description(description)
			.phone(phone)
			.imageUrl(imageUrl)
			.openTime(openTime)
			.closeTime(closeTime)
			.deliveryMinMinutes(deliveryMinMinutes)
			.deliveryMaxMinutes(deliveryMaxMinutes)
			.deliveryFee(deliveryFee)
			.minimumOrderAmount(minimumOrderAmount)
			.build();
	}
}