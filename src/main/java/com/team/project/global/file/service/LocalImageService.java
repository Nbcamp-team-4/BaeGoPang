package com.team.project.global.file.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.team.project.global.file.ImageType;

@Service
public class LocalImageService implements ImageService {

	private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Override
	public String upload(MultipartFile file, ImageType imageType) {
		validateFile(file);

		String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
		String extension = extractExtension(originalFilename);
		String savedFileName = UUID.randomUUID() + "." + extension;

		Path saveDir = Paths.get(uploadDir, imageType.getDirName());
		Path savePath = saveDir.resolve(savedFileName);

		try {
			Files.createDirectories(saveDir);
			file.transferTo(savePath);
		} catch (IOException e) {
			throw new RuntimeException("이미지 저장에 실패했습니다.", e);
		}

		// TODO: 추후 S3 전환 시 이 반환값은 "/images/..."가 아니라
		// TODO: S3 또는 CloudFront의 전체 URL로 변경될 수 있음
		// 예: https://cdn.example.com/products/uuid.png
		return "/images/" + imageType.getDirName() + "/" + savedFileName;
	}

	private void validateFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("업로드할 파일이 없습니다.");
		}

		if (file.getSize() > MAX_FILE_SIZE) {
			throw new IllegalArgumentException("이미지 파일은 5MB 이하만 업로드할 수 있습니다.");
		}

		String contentType = file.getContentType();
		if (contentType == null || !isImageContentType(contentType)) {
			throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
		}

		String originalFilename = file.getOriginalFilename();
		String extension = extractExtension(originalFilename);
		if (!isAllowedExtension(extension)) {
			throw new IllegalArgumentException("jpg, jpeg, png, webp 파일만 업로드할 수 있습니다.");
		}
	}

	private boolean isImageContentType(String contentType) {
		return contentType.equals("image/jpeg")
			|| contentType.equals("image/png")
			|| contentType.equals("image/webp");
	}

	private boolean isAllowedExtension(String extension) {
		return extension.equals("jpg")
			|| extension.equals("jpeg")
			|| extension.equals("png")
			|| extension.equals("webp");
	}

	private String extractExtension(String fileName) {
		if (fileName == null || !fileName.contains(".")) {
			throw new IllegalArgumentException("확장자가 없는 파일입니다.");
		}

		return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
	}
}