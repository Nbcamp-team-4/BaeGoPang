package com._team._project.domain.store.api.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateStoreRequest {

    private String name;

    // 프론트에서 위도/경도로 받는 형태 (Point 변환은 service/util에서 처리)
    private Double latitude;
    private Double longitude;

    // region
    private UUID regionId;
}
