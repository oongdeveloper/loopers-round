package com.loopers.event.core;

import com.loopers.event.core.payload.LikeAppEvent;
import com.loopers.event.core.payload.OrderAppEvent;
import com.loopers.event.core.payload.ProductAppEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
    // 상품 상세 조회
    // 좋아요

    PRODUCT_DETAIL_CLICKED(ProductAppEvent.Clicked.class, Topic.CATALOG),
    PRODUCT_LIKED(LikeAppEvent.Liked.class, Topic.CATALOG),
    PRODUCT_UNLIKED(LikeAppEvent.UnLiked.class, Topic.CATALOG),
    ORDER_CREATED(OrderAppEvent.Created.class, Topic.ORDER),
    ORDER_COMPLETED(OrderAppEvent.Completed.class, Topic.ORDER),
    ORDER_CANCELED(OrderAppEvent.Canceled.class, Topic.ORDER),
    ;

    private final Class<? extends EventPayload> payloadClass;
    private final String topic;

    public static EventType from(String type) {
        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("[EventType.from] type={}", type, e);
            return null;
        }
    }

    public static class Topic {
        public static final String CATALOG = "catalog-events";
        public static final String ORDER = "order-events";
    }
}
