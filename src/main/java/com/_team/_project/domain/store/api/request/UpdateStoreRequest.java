package com._team._project.domain.store.api.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateStoreRequest {

    private String name;

    private Double latitude;
    private Double longitude;

    // region 자동 판별/권한 정책에 따라 수정 불가일 수 있음 (2차)
    private UUID regionId;
}
