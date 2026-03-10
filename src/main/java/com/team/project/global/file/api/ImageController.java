package com.team.project.global.file.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team.project.global.file.ImageType;
import com.team.project.global.file.dto.UploadImageResponse;
import com.team.project.global.file.service.ImageService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;

	@Operation(summary = "가게 이미지 업로드")
	@PostMapping(value = "/stores", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<UploadImageResponse> uploadStoreImage(
			@RequestPart("file") MultipartFile file
	) {
		String imageUrl = imageService.upload(file, ImageType.STORE);
		return ResponseEntity.ok(UploadImageResponse.from(imageUrl));
	}

	@Operation(summary = "상품 이미지 업로드")
	@PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<UploadImageResponse> uploadProductImage(
			@RequestPart("file") MultipartFile file
	) {
		String imageUrl = imageService.upload(file, ImageType.PRODUCT);
		return ResponseEntity.ok(UploadImageResponse.from(imageUrl));
	}
	@Operation(summary = "리뷰 이미지 업로드")
	@PostMapping(value = "/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<UploadImageResponse> uploadReviewImage(
		@RequestPart("file") MultipartFile file
	) {
		String imageUrl = imageService.upload(file, ImageType.REVIEW);
		return ResponseEntity.ok(UploadImageResponse.from(imageUrl));
	}
}