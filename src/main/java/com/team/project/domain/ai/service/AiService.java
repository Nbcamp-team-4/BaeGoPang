package com.team.project.domain.ai.service;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.ai.api.request.ProcessAiRecommendRequest;
import com.team.project.domain.ai.api.response.GetReviewReplyResponse;
import com.team.project.domain.ai.api.response.SearchAiRecommendResponse;

public interface AiService {

	List<SearchAiRecommendResponse> recommendMenu(ProcessAiRecommendRequest request);

	GetReviewReplyResponse generateReviewReply(UUID reviewId, String tone);
}