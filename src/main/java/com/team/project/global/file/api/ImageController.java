package com.team.project.global.file.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team.project.global.file.ImageType;
import com.team.project.global.file.dto.UploadImageResponse;
import com.team.project.global.file.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;

	@PostMapping("/api/images/stores")
	public ResponseEntity<UploadImageResponse> uploadStoreImage(@ModelAttribute MultipartFile file) {
		String imageUrl = imageService.upload(file, ImageType.STORE);
		return ResponseEntity.ok(UploadImageResponse.from(imageUrl));
	}

	@PostMapping("/api/images/products")
	public ResponseEntity<UploadImageResponse> uploadProductImage(@ModelAttribute MultipartFile file) {
		String imageUrl = imageService.upload(file, ImageType.PRODUCT);
		return ResponseEntity.ok(UploadImageResponse.from(imageUrl));
	}

	// TODO: 추후 S3 presigned URL 방식으로 바꿀 경우
	// TODO: 업로드 API 자체가 "파일 업로드"가 아니라
	// TODO: "업로드용 presigned URL 발급 API"로 바뀔 수 있음
}