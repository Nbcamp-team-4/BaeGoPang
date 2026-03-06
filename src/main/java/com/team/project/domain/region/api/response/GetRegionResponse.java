package com.team.project.domain.region.api.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetRegionResponse {

    private UUID id;
    private String name;
    private String geomWkt;   // 상세에서 보임
    private Boolean isActive;

}