package com.team.project.domain.ai.api.response;

import java.util.UUID;

import lombok.Builder;

@Builder
public record GetReviewReplyAiResponse(
	UUID reviewId,
	String originalContent,
	String aiGeneratedReply
) {}