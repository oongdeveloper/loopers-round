package com.loopers.interfaces.api.catalog;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Brand V1 API", description = "Brand 전시 관련 API 입니다.")
public interface BrandV1ApiSpec {

    @Operation(
            summary = "Brand 목록 조회",
            description = "Brand 목록을 조회합니다."
    )
    ApiResponse<?> getList(
            @Schema(name = "브랜드 ID")
            Long brandId
    );
}
