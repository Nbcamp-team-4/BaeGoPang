package com.team.project.domain.order.api.request;

import java.time.LocalDateTime;

import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.global.common.dto.BasePageRequest;
import com.team.project.global.common.dto.BaseRangeRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "고객 주문 목록 조회 요청")
public class GetOrdersRequest extends BasePageRequest {

    @Schema(description = "주문 상태", example = "PAID")
    private OrderStatus status;

    @Schema(description = "주문 생성일 범위")
    private BaseRangeRequest<LocalDateTime> rangeCreatedAt;
}