package com.loopers.application.metric;

import com.loopers.domain.handled.HandledService;
import com.loopers.domain.metric.ProductMetric;
import com.loopers.domain.metric.ProductMetricService;
import com.loopers.event.core.EventEnvelop;
import com.loopers.event.core.EventPayload;
import com.loopers.event.core.EventType;
import com.loopers.event.core.payload.LikeAppEvent;
import com.loopers.event.core.payload.OrderAppEvent;
import com.loopers.event.core.payload.ProductAppEvent;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class ProductMetricFacade {
    private final HandledService handledService;
    private final ProductMetricService productMetricService;

    public ProductMetricFacade(HandledService handledService, ProductMetricService productMetricService) {
        this.handledService = handledService;
        this.productMetricService = productMetricService;
    }

    @Transactional
    public void handle(EventEnvelop<EventPayload> envelop){
        if(isAlreadyDone(envelop.getEventId())) return;

        switch (envelop.getType()){
            case EventType.PRODUCT_DETAIL_CLICKED:
                ProductAppEvent.Clicked clicked = (ProductAppEvent.Clicked) envelop.getPayload();
                ProductMetric clickedMetric = productMetricService.findById(clicked.productId(), envelop.getCreatedAt().toLocalDate());
                clickedMetric.increaseViewCount();
                break;
            case EventType.PRODUCT_LIKED:
                LikeAppEvent.Liked liked = (LikeAppEvent.Liked) envelop.getPayload();
                ProductMetric likedMetric = productMetricService.findById(liked.productId(), envelop.getCreatedAt().toLocalDate());
                likedMetric.increaseLikeCount();
                break;
            case EventType.PRODUCT_UNLIKED:
                LikeAppEvent.UnLiked unLiked = (LikeAppEvent.UnLiked) envelop.getPayload();
                ProductMetric unLikedMetric = productMetricService.findById(unLiked.productId(), envelop.getCreatedAt().toLocalDate());
                unLikedMetric.decreaseLikeCount();
                break;
            case EventType.ORDER_COMPLETED:
                OrderAppEvent.Completed completed = (OrderAppEvent.Completed) envelop.getPayload();
                completed.orderedProductId().forEach(
                        (skuId, productId) -> {
                            ProductMetric completedMetric = productMetricService.findById(productId, envelop.getCreatedAt().toLocalDate());
                            completedMetric.increaseSaleCount();
                        }
                );
                break;
            case EventType.ORDER_CANCELED:
                OrderAppEvent.Canceled canceled = (OrderAppEvent.Canceled) envelop.getPayload();
                canceled.orderedProductId().forEach(
                        (skuId, productId) -> {
                            ProductMetric canceledMetric = productMetricService.findById(productId, envelop.getCreatedAt().toLocalDate());
                            canceledMetric.decreaseSaleCount();
                        }
                );
                break;
            default:
                break;
        }
    }

    private boolean isAlreadyDone(String envelopId){
        return handledService.findByEventId(envelopId)
                .isPresent();
    }
}
