package com.team.project.domain.ai.api.request;

import java.util.UUID; // <--- 이 줄이 반드시 추가되어야 합니다!

public record ProcessReviewReplyAiRequest(
	UUID reviewId,     // 이제 자바가 UUID를 인식합니다.
	String tone        // 답글 스타일
) {}