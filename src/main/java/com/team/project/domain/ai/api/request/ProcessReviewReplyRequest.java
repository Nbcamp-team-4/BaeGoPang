package com.team.project.domain.ai.api.request;

import java.util.UUID;

public record ProcessReviewReplyRequest(
	UUID reviewId
) {}