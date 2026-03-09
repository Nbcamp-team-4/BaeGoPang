package com.team.project.global.file.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadImageResponse {

	private String imageUrl;

	public static UploadImageResponse from(String imageUrl) {
		return UploadImageResponse.builder()
			.imageUrl(imageUrl)
			.build();
	}
}