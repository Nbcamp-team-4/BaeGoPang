package com.team.project.domain.store.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode {

	// 400: 요청값 또는 상태값이 잘못됨
	INVALID_STORE_REQUEST("요청 값이 올바르지 않습니다."),
	INVALID_DELIVERY_FEE("배달비는 0원 이상이어야 합니다."),
	INVALID_DELIVERY_TIME("배달 시간 범위가 올바르지 않습니다."),
	INVALID_STORE_TIME("마감 시간은 오픈 시간 이후여야 합니다."),
	INVALID_MIN_DELIVERY_TIME("배달시간은 음수일 수 없습니다."),
	INVALID_MINIMUM_ORDER_AMOUNT("최소 주문 금액은 음수일 수 없습니다."),
	STORE_NOT_OPERATING("현재 운영 중이 아닙니다."),

	// 403: 권한 문제
	STORE_FORBIDDEN("해당 가게에 대한 권한이 없습니다."),

	// 404: 리소스 없음
	STORE_NOT_FOUND("가게를 찾을 수 없습니다.");

	private final String message;
}