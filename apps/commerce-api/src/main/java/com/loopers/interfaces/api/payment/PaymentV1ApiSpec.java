package com.loopers.interfaces.api.payment;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Payment V1 API", description = "결제 관련 API 입니다.")
public interface PaymentV1ApiSpec {

    @Operation(
            summary = "결제 요청",
            description = "결제를 요청합니다."
    )
    ApiResponse<?> payment(
            @Schema(name = "사용자 ID")
            Long userId,
            @Schema(name = "결제 정보", description = "결제한 주문정보와 사용자 정보(카드 등)")
            PaymentV1Dto.Request request
    );

    @Operation(
            summary = "결제키 생성",
            description = "결제키 생성을 요청합니다."
    )
    ApiResponse<String> generateKey(
            @Schema(name = "결제키 생성 정보", description = "사용자 ID, 주문 ID")
            PaymentV1Dto.UniqueKeyReq keyRequest
    );
}
