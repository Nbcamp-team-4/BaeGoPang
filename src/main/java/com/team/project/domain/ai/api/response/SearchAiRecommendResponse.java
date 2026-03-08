package com.team.project.domain.ai.api.response;

import lombok.Builder;

/**
 * AI 메뉴 추천 결과 목록 조회를 위한 응답 DTO
 */
@Builder
public record SearchAiRecommendResponse(
	String name,        // 메뉴명 (예: "돼지국밥")
	String storeName,   // 가게명 (예: "광화문국밥")
	Integer price,      // 가격 (예: 9500)
	String description, // 추천 사유 또는 메뉴 설명
	Integer matchRate   // AI 분석 일치율 (%)
) {}