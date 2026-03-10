package com.team.project.global.file.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.team.project.global.file.ImageType;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Profile("prod")
@RequiredArgsConstructor
public class S3ImageService implements ImageService {

	private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

	private final S3Client s3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Override
	public String upload(MultipartFile file, ImageType imageType) {
		validateFile(file);

		String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
		String extension = extractExtension(originalFilename);
		String savedFileName = UUID.randomUUID() + "." + extension;
		String key = imageType.getDirName() + "/" + savedFileName;

		try {
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
					.bucket(bucket)
					.key(key)
					.contentType(file.getContentType())
					.build();

			s3Client.putObject(
					putObjectRequest,
					RequestBody.fromBytes(file.getBytes())
			);
		} catch (Exception e) {
			throw new RuntimeException("S3 이미지 업로드에 실패했습니다.", e);
		}

		return buildFileUrl(key);
	}

	private String buildFileUrl(String key) {
		return "https://" + bucket + ".s3." + region + ".amazonaws.com/"
				+ URLEncoder.encode(key, StandardCharsets.UTF_8)
				.replace("+", "%20")
				.replace("%2F", "/");
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