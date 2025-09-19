package com.loopers.interfaces.api.ranking;

import com.loopers.application.product.ProductInfo;
import com.loopers.application.ranking.RankingFacade;
import com.loopers.interfaces.api.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class RankingController implements RankingV1ApiSpec{
    private final RankingFacade rankingFacade;

    public RankingController(RankingFacade rankingFacade) {
        this.rankingFacade = rankingFacade;
    }

    @Override
    @GetMapping(value = "/api/v1/rankings", consumes = "applciation/json")
    public ApiResponse<?> get(@RequestParam("date") LocalDate today,
                              @RequestParam("period") String period,
                             @RequestParam("size") int size,
                             @RequestParam("page") int page) {
        Page<ProductInfo.DataList> result = rankingFacade.getProductRanking(today, period, size, page);
        return ApiResponse.success(result);
//        return ApiResponse.success(result.stream().map(RankV1Dto.Summary::from)
//                .collect(Collectors.toList()));
    }
}
