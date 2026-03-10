package com.team.project.domain.address.api.request;

import com.team.project.global.common.dto.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "내 주소 목록 조회 요청")
public class GetUserAllAddressRequest  extends BasePageRequest {
}
