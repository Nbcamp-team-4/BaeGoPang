package com.team.project.domain.ai.api.request;

/**
 * AI 메뉴 추천 비즈니스 로직 실행을 위한 요청 DTO
 */
public record ProcessAiRecommendRequest(
	String mood,   // 오늘 어떤 음식이 땡기나요? (예: "🥣 따뜻한 국물")
	String people, // 몇 명이서 드시나요? (예: "혼자")
	String budget  // 예산은 얼마인가요? (예: "1만원 이하")
) {}