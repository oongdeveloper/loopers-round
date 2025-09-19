package com.loopers.application.ranking;

import com.loopers.application.product.ProductInfo;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.projections.ProductListProjectionV2;
import com.loopers.domain.ranking.RankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Slf4j
@Component
public class RankingFacade {
    private final RankingService rankingService;
    private final ProductService productService;
    private final String RANKING_KEY = "ranking:product:";
    private final String DEFAULT_RANKING = "ranking:product:default";

    public RankingFacade(RankingService rankingService, ProductService productService) {
        this.rankingService = rankingService;
        this.productService = productService;
    }

    public Page<ProductInfo.DataList> getProductRanking(LocalDate today, String period, int size, int page){
        int start = size * page;
        int end = start + size;
//        String rankKey = RANKING_KEY + today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String rankKey = getRankingKey(period);

        Set<Long> rankIds = rankingService.getRange(rankKey, start, end);
        Long count = rankingService.count(rankKey);
        // 조회가 안되는 경우, 고려 필요
        if(count == 0){
            log.info("Redis 에 Ranking 정보가 존재하지 않습니다.");
            rankIds = rankingService.getRange(DEFAULT_RANKING, start, end);
        }

        List<ProductListProjectionV2> productList = productService.getProductListByIds(rankIds);

        return new PageImpl<>(productList, PageRequest.of(page, size), count)
                .map(pj ->
                        new ProductInfo.DataList(
                                pj.getId(),
                                pj.getBrandName(),
                                pj.getProductName(),
                                pj.getPrice(),
                                pj.getImageUrl(),
                                pj.getDescription(),
                                pj.getPublishedAt(),
                                pj.getLikeCount()
                        ));
    }

    private String getRankingKey(String period){
        Period period1 = Period.valueOf(period);
        String rankingKey = "";
        LocalDate today = LocalDate.now();

        switch (period1){
            case DAILY:
                rankingKey = RANKING_KEY + today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                break;
            case WEEKLY:
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                int weekNumber = today.get(weekFields.weekOfWeekBasedYear());
                int year = today.get(weekFields.weekBasedYear());
                rankingKey = RANKING_KEY + String.format("%d-%02d", year, weekNumber);
                break;
            case MONTHLY:
                YearMonth yearMonth = YearMonth.from(today);
                rankingKey = RANKING_KEY + yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                break;
            default:
                rankingKey = DEFAULT_RANKING;
                break;
        }
        return rankingKey;
    }

    enum Period{
        DAILY,
        WEEKLY,
        MONTHLY
    }

}
