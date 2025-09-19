package com.loopers.interfaces.api.ranking;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;

@Tag(name = "Point V1 API", description = "Point 기능 API 입니다.")
public interface RankingV1ApiSpec {

    @Operation(
            summary = "랭킹 조회",
            description = "오늘 날짜의 랭킹 조회"
    )
    ApiResponse<?> get(
            @Schema(name = "오늘 날짜", description = "조회할 오늘 랭킹 날짜")
            LocalDate date,
            @Schema(name = "날짜 옵션", description = "일간/주간/월간")
            String period,
            @Schema(name = "Page Size", description = "조회 Page Size")
            int size,
            @Schema(name = "Page Num", description = "Page 번호")
            int page
    );
}
