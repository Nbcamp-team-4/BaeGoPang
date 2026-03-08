package com.team.project.domain.ai.api;

import com.team.project.domain.ai.api.request.ProcessAiRecommendRequest;
import com.team.project.domain.ai.api.response.SearchAiRecommendResponse;
import com.team.project.domain.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

	private final AiService aiService;

	@PostMapping("/recommend")
	public List<SearchAiRecommendResponse> getRecommend(@RequestBody ProcessAiRecommendRequest request) {
		// 내부적으로 SearchAiRecommendQuery 등을 활용하여 로직 수행
		return aiService.recommendMenu(request);
	}
}