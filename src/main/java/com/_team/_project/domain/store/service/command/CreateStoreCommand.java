package com._team._project.domain.store.service.command;

import java.time.LocalTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateStoreCommand {
	private final UUID userId;
	private final UUID regionId;
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
}
