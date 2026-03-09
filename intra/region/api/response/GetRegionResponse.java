package com.team.project.domain.region.api.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class GetRegionResponse {

    private UUID id;
    private String name;
    private String geomWkt;   // 상세에서 보임
    private Boolean isActive;

}