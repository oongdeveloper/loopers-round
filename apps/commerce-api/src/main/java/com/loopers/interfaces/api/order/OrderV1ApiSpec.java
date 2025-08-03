package com.loopers.interfaces.api.order;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Order V1 API", description = "주문 관련 API 입니다.")
public interface OrderV1ApiSpec {
    @Operation(
            summary = "주문 생성",
            description = "주문을 생성합니다."
    )
    ApiResponse<?> create(
            @Schema(name = "사용자 ID")
            String userId,
            @Schema(name = "주문 상품 정보", description = "주문할 상품과 수량 정보")
            OrderV1Dto.CreateRequest request
    );


    @Operation(
            summary = "주문 목록 조회",
            description = "주문 목록을 조회합니다."
    )
    ApiResponse<?> getList(
            @Schema(name = "사용자 ID")
            String userId
    );

    @Operation(
            summary = "주문 상세 조회",
            description = "주문의 상세 정보를 조회합니다."
    )
    ApiResponse<?> getDetail(
            @Schema(name = "사용자 ID")
            String userId,
            @Schema(name = "주문 ID")
            Long orderId
    );
}
