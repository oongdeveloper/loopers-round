package com.loopers.application.metric;

import com.loopers.domain.handled.HandledService;
import com.loopers.domain.metric.ProductMetric;
import com.loopers.domain.metric.ProductMetricService;
import com.loopers.domain.ranking.RankingService;
import com.loopers.event.core.EventEnvelop;
import com.loopers.event.core.EventPayload;
import com.loopers.event.core.EventType;
import com.loopers.event.core.payload.LikeAppEvent;
import com.loopers.event.core.payload.OrderAppEvent;
import com.loopers.event.core.payload.ProductAppEvent;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductMetricFacade {
    private final HandledService handledService;
    private final ProductMetricService productMetricService;
    private final RankingService rankingService;
    private final String VIEW_WEIGHT_KEY = "ranking:weight:view";
    private final String LIKE_WEIGHT_KEY = "ranking:weight:like";
    private final String ORDER_WEIGHT_KEY = "ranking:weight:order";
    private final String RANKING_KEY = "ranking:product:";

    public ProductMetricFacade(HandledService handledService, ProductMetricService productMetricService, RankingService rankingService) {
        this.handledService = handledService;
        this.productMetricService = productMetricService;
        this.rankingService = rankingService;
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

    @Transactional
    public void handle(List<AggregateEvent> aggregateEvents){
       // TODO. grouping
        Map<Long, Map<EventType, Long>> aggregatedEvents = aggregateEvents.stream()
                .flatMap(event -> event.getProductIds().stream()
                        .map(productId -> new AbstractMap.SimpleEntry<>(productId, event.getType())))
                .collect(Collectors.groupingBy(
                        AbstractMap.SimpleEntry::getKey,
                        Collectors.groupingBy(AbstractMap.SimpleEntry::getValue, Collectors.counting())
                ));

        // Score 계산
        Float viewWeight = Optional.ofNullable(rankingService.getWeight(VIEW_WEIGHT_KEY))
                .orElse(0.1f);
        Float likeWeight = Optional.ofNullable(rankingService.getWeight(LIKE_WEIGHT_KEY))
                .orElse(0.5f);
        Float orderWeight = Optional.ofNullable(rankingService.getWeight(ORDER_WEIGHT_KEY))
                .orElse(0.8f);

        Map<String, Double> finalScores = new HashMap<>();
        aggregatedEvents.forEach((productId, actionCounts) -> {
            long viewCount = actionCounts.getOrDefault(EventType.PRODUCT_DETAIL_CLICKED, 0L);
            long likeCount = actionCounts.getOrDefault(EventType.PRODUCT_LIKED, 0L);
            long unlikeCount = actionCounts.getOrDefault(EventType.PRODUCT_UNLIKED, 0L);
            long orderCount = actionCounts.getOrDefault(EventType.ORDER_COMPLETED, 0L);
            long orderCanceledCount = actionCounts.getOrDefault(EventType.ORDER_CANCELED, 0L);

            double score = viewCount * viewWeight
                    + (likeCount - unlikeCount) * likeWeight
                    + (orderCount - orderCanceledCount) * orderWeight;

            finalScores.put(String.valueOf(productId), score);
        });

        // Redis 에 저장
        rankingService.add(RANKING_KEY + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), finalScores);

        // Product Metric 에 저장
        aggregatedEvents.forEach((productId, actionCounts) -> {
            ProductMetric metric = productMetricService.findById(productId, LocalDate.now());

            metric.increaseViewCount(actionCounts.getOrDefault(EventType.PRODUCT_DETAIL_CLICKED, 0L));
            metric.increaseLikeCount(actionCounts.getOrDefault(EventType.PRODUCT_LIKED, 0L));
            metric.decreaseLikeCount(actionCounts.getOrDefault(EventType.PRODUCT_UNLIKED, 0L));
            metric.increaseSaleCount(actionCounts.getOrDefault(EventType.ORDER_COMPLETED, 0L));
            metric.decreaseSaleCount(actionCounts.getOrDefault(EventType.ORDER_CANCELED, 0L));
        });
    }

    private boolean isAlreadyDone(String envelopId){
        return handledService.findByEventId(envelopId)
                .isPresent();
    }
}
