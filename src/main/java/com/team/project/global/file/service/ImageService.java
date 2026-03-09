package com.team.project.global.file.service;

import org.springframework.web.multipart.MultipartFile;

import com.team.project.global.file.ImageType;

public interface ImageService {

	String upload(MultipartFile file, ImageType imageType);
}