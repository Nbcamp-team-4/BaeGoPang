package com.team.project.domain.store.service.command;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateOwnerFieldsCommand {
	private final UUID storeId;
	private final UUID userId;
	// 기본 정보
	private final String description;
	private final String phone;
	private final String imageUrl;
	// 운영 시간
	private final LocalTime openTime;
	private final LocalTime closeTime;
	//배달 설정 (점주가 상황에 따라 조정 가능해야 함)
	private final Integer deliveryMinMinutes;   // 최소 배달 시간
	private final Integer deliveryMaxMinutes;   // 최대 배달 시간
	private final Integer deliveryFee;          // 배달 팁
	private final Integer minimumOrderAmount;   // 최소 주문 금액
	List<UUID> categoryIds;
}