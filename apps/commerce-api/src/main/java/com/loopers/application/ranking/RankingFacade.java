package com.loopers.application.ranking;

import com.loopers.application.product.ProductInfo;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.projections.ProductListProjectionV2;
import com.loopers.domain.ranking.RankingService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RankingFacade {
    private final RankingService rankingService;
    private final ProductService productService;
    private final String RANKING_KEY = "ranking:product:";
    public RankingFacade(RankingService rankingService, ProductService productService) {
        this.rankingService = rankingService;
        this.productService = productService;
    }

    public List<ProductInfo.DataList> getProductRanking(LocalDate today, int size, int page){
        int start = size * page;
        int end = start + size;
        Set<Long> rankIds = rankingService.getRange(RANKING_KEY + today.format(DateTimeFormatter.ofPattern("yyyyMMdd")), start, end);
        // 조회가 안되는 경우, 고려 필요
        List<ProductListProjectionV2> productList = productService.getProductListByIds(rankIds);
        return productList.stream().map(pj -> {
                    return new ProductInfo.DataList(
                            pj.getId(),
                            pj.getBrandName(),
                            pj.getProductName(),
                            pj.getPrice(),
                            pj.getImageUrl(),
                            pj.getDescription(),
                            pj.getPublishedAt(),
                            pj.getLikeCount()
                    );
                })
                .collect(Collectors.toList());
    }
}
