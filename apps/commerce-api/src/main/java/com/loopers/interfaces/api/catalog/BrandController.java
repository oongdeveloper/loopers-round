package com.loopers.interfaces.api.catalog;

import com.loopers.application.brand.BrandFacade;
import com.loopers.application.brand.BrandInfo;
import com.loopers.application.brand.BrandQuery;
import com.loopers.interfaces.api.ApiResponse;
import org.springframework.stereotype.Controller;

@Controller
public class BrandController implements BrandV1ApiSpec{
    private final BrandFacade brandFacade;

    public BrandController(BrandFacade brandFacade) {
        this.brandFacade = brandFacade;
    }

    @Override
    public ApiResponse<?> getList(Long brandId) {
        BrandInfo result = brandFacade.getBrandDetail(BrandQuery.Detail.of(brandId));
        return null;
    }
}
