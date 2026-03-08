package com.team.project.domain.ai.service;

import java.util.List;

import com.team.project.domain.ai.api.request.ProcessAiRecommendRequest;
import com.team.project.domain.ai.api.response.SearchAiRecommendResponse;

public interface AiService {
	// String이 아닌 ProcessAiRecommendRequest를 받도록 정의
	List<SearchAiRecommendResponse> recommendMenu(ProcessAiRecommendRequest request);
}