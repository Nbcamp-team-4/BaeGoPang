package com.team.project.domain.ai.service;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.ai.api.request.ProcessAiRecommendRequest;
import com.team.project.domain.ai.api.response.GetReviewReplyResponse;
import com.team.project.domain.ai.api.response.SearchAiRecommendResponse;

public interface AiService {
	List<SearchAiRecommendResponse> recommendMenu(ProcessAiRecommendRequest request);

	// 2. 여기 리턴 타입을 String에서 GetReviewReplyResponse로 수정!
	GetReviewReplyResponse generateReviewReply(UUID reviewId, String tone);
}