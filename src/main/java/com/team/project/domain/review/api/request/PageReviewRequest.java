package com.team.project.domain.review.api.request;

import com.team.project.global.common.dto.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // GET 요청의 쿼리 파라미터를 바인딩하기 위해 필요합니다.
public class PageReviewRequest extends BasePageRequest {

	@Schema(description = "정렬 기준", example = "createdAt", allowableValues = {"createdAt", "rating"})
	private String sortBy = "createdAt";

	@Schema(description = "내림차순 여부", example = "true")
	private boolean isDesc = true;
}