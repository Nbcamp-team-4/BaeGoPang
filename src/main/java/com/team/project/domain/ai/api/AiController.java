package com.team.project.domain.ai.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.ai.service.AiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

	private final AiService aiService;

	@GetMapping("/recommend")
	public String getRecommend(@RequestParam(name = "q") String query) {
		return aiService.recommendMenu(query);
	}
}