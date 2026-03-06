package com._team._project.domain.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiServiceConfig {

	@Bean
	public ChatClient chatClient(ChatClient.Builder builder) {
		// ChatClient.Builder는 Spring AI가 자동으로 제공합니다.
		// 이를 통해 실제 주입받을 ChatClient 빈을 생성합니다.
		return builder.build();
	}
}