package com.team.project.global.file;

import lombok.Getter;

@Getter
public enum ImageType {
	STORE("stores"),
	PRODUCT("products");

	private final String dirName;

	ImageType(String dirName) {
		this.dirName = dirName;
	}

	}