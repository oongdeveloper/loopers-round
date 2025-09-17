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
import java.time.format.DateTimeFormatter;
import java.util.List;
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

    public Page<ProductInfo.DataList> getProductRanking(LocalDate today, int size, int page){
        int start = size * page;
        int end = start + size;
        String rankKey = RANKING_KEY + today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

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

//        return productList.stream().map(pj -> {
//                    return new ProductInfo.DataList(
//                            pj.getId(),
//                            pj.getBrandName(),
//                            pj.getProductName(),
//                            pj.getPrice(),
//                            pj.getImageUrl(),
//                            pj.getDescription(),
//                            pj.getPublishedAt(),
//                            pj.getLikeCount()
//                    );
//                })
//                .collect(Collectors.toList());
    }
}
