package com.team.project.domain.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.ai.entity.AiLog;
import com.team.project.domain.ai.entity.AiTaskType;
import com.team.project.domain.ai.repository.AiLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AiService {

	private final AiLogRepository aiLogRepository;
	private final ChatClient chatClient; // Spring AI가 자동으로 주입해줍니다.

	public String recommendMenu(String userRequest) {
		// 1. 페르소나 설정 및 질문 던지기
		String aiResponse = chatClient.prompt()
			.system("당신은 배달 앱 '배고팡'의 친절한 메뉴 추천 전문가입니다. "
				+ "사용자의 기분이나 상황에 맞춰 우리 앱의 카테고리(한식, 중식, 일식, 치킨, 피자 등) 내에서 메뉴를 추천해주세요.")
			.user(userRequest)
			.call()
			.content();

		// 2. DB에 로그 저장 (BaseEntity 덕분에 시간/작성자 자동 저장)
		AiLog log = AiLog.builder()
			.requestText(userRequest)
			.responseText(aiResponse)
			.taskType(AiTaskType.MENU_RECOMMENDATION)
			.build();

		aiLogRepository.save(log);

		return aiResponse;
	}
}