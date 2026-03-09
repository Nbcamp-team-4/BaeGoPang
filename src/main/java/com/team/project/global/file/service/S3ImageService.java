package com.team.project.global.file.service;

import org.springframework.web.multipart.MultipartFile;

import com.team.project.global.file.ImageType;

// TODO: S3 연동 시 활성화할 서비스
// TODO: AWS SDK, bucket, region, credentials 설정 후 구현
//@Service
public class S3ImageService implements ImageService {

	@Override
	public String upload(MultipartFile file, ImageType imageType) {
		// TODO: 1. 파일 검증
		// TODO: 2. S3 key 생성 (ex: products/uuid.png)
		// TODO: 3. s3Client.putObject(...) 업로드
		// TODO: 4. S3 public URL 또는 CloudFront URL 반환

		throw new UnsupportedOperationException("S3 업로드는 아직 구현되지 않았습니다.");
	}
}