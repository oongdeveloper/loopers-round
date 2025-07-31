package com.loopers.application.catalog;

import com.loopers.domain.catalog.Brand;
import com.loopers.domain.catalog.BrandCatalogService;
import com.loopers.domain.catalog.ProductCatalog;
import com.loopers.domain.catalog.ProductCatalogService;
import com.loopers.domain.product.ProductSku;
import com.loopers.domain.product.ProductSkuService;
import com.loopers.domain.product.SkuOption;
import com.loopers.interfaces.api.catalog.ProductCatalogV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductCatalogFacade {
    private final BrandCatalogService brandCatalogService;
    private final ProductCatalogService productCatalogService;
    private final ProductSkuService productSkuService;

    public ProductCatalogFacade(BrandCatalogService brandCatalogService, ProductCatalogService productCatalogService, ProductSkuService productSkuService) {
        this.brandCatalogService = brandCatalogService;
        this.productCatalogService = productCatalogService;
        this.productSkuService = productSkuService;
    }

    public ProductDetailInfo retrieveProductDetail(Long productId){
// 1. ProductCatalog 정보 조회
        // findById 결과를 Optional로 받고, 없으면 NOT_FOUND 예외 발생
        ProductCatalog productCatalog = productCatalogService.findById(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

        // 2. Brand 정보 조회 (ProductCatalog의 brandId 사용)
        Brand brand = brandCatalogService.find(productCatalog.getBrandId()) // find(id)는 Optional<Brand> 반환 가정
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "브랜드를 찾을 수 없습니다.")); // 이 경우는 매우 드물어야 함 (데이터 무결성)

        // 3. 해당 ProductCatalog에 속한 모든 ProductSku 조회
        List<ProductSku> skus = productSkuService.findByProductCatalogId(productId);

        // 4. 각 ProductSku에 대한 Option 정보 조회 및 조합
        // SKU가 많을 경우 N+1 쿼리를 피하기 위해 SkuOption, OptionName, OptionValue를 JOIN FETCH로 한 번에 가져오는 쿼리가 ProductSkuService 내부에 필요
        // 또는, 조회된 모든 SkuOption에서 필요한 OptionName/Value ID를 추출하여 한 번에 OptionName, OptionValue를 조회 후 Map으로 변환

        // 4-1. 모든 SKU 옵션들을 조회 (SKU-OptionName-OptionValue 조인을 통해)
        // ProductSkuService에 이런 메서드가 있다고 가정:
        // List<ProductSkuWithOptionInfo> skusWithOptions = productSkuService.findSkusWithOptionsByProductCatalogId(productCatalogId);

        // 또는 수동으로 매핑:
        List<ProductDetailInfo.SkuInfo> skuInfos = skus.stream().map(sku -> {
            // 각 SKU에 연결된 SkuOption들을 조회 (N+1 문제 가능성 있으므로, SkuOption fetch join 고려)
            List<SkuOption> skuOptions = sku.getSkuOptions(); // SkuOption 엔티티에 @OneToMany(mappedBy="productSku", fetch = FetchType.EAGER) 또는 별도 서비스 호출

            // SkuOption에서 OptionName과 OptionValue 정보 추출
            List<ProductDetailInfo.OptionDetail> optionDetails = skuOptions.stream()
                    .map(so -> new ProductDetailInfo.OptionDetail(
                            so.getOptionName().getName(),
                            so.getOptionValue().getValue()
                    ))
                    .collect(Collectors.toList());

            return new ProductDetailInfo.SkuInfo(
                    sku.getId(),
                    sku.getUnitPrice(),
                    sku.getImageUrl(),
                    sku.getStatus().getCode(), // SkuStatus의 description 사용
                    optionDetails
            );
        }).collect(Collectors.toList());


        // 5. 모든 정보를 조합하여 ProductDetailInfo DTO 생성 및 반환
        return new ProductDetailInfo(
                productCatalog.getId(),
                brand.getBrandName(), // Brand 이름 사용
                productCatalog.getProductName(),
                productCatalog.getBasePrice(),
                productCatalog.getImageUrl(),
                productCatalog.getDescription(),
                productCatalog.getPublishedAt(),
                skuInfos
        );
    }

    public Page<ProductCatalogInfo> retrieveProductCatalog(Optional<Long> brandId, ProductCatalogV1Dto.ProductCatalogRequest request){
        Sort sort = Sort.by(Sort.Direction.fromString(request.direction().name()), request.sortBy().getPropertyName());
        Pageable pageable = PageRequest.of(request.page(), request.size(), sort);

        brandId.ifPresent(id -> brandCatalogService.find(id)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "Brand 를 찾을 수 없습니다.")));
        Page<ProductCatalog> productCatalogs = productCatalogService.find(brandId, pageable);
        return convertToProductCatalogInfoPage(productCatalogs);
    }

    private Page<ProductCatalogInfo> convertToProductCatalogInfoPage(Page<ProductCatalog> productCatalogsPage) {
        List<ProductCatalog> productCatalogs = productCatalogsPage.getContent();
        // 1. ProductCatalog 리스트에서 모든 고유한 brandId를 추출
        Set<Long> brandIds = productCatalogs.stream()
                .map(ProductCatalog::getBrandId) // ProductCatalog에 getBrandId()가 있다고 가정
                .collect(Collectors.toSet()); // 중복 제거를 위해 Set 사용

        // 2. 추출된 brandId들을 사용하여 모든 Brand 정보를 한 번에 조회 (단일 쿼리)
        List<Brand> brands = brandCatalogService.findAllById(brandIds);

        // 3. Brand 리스트를 brandId를 키로, brandName을 값으로 하는 Map으로 변환
        //    (조회 속도를 높이기 위함)
        Map<Long, String> brandIdToNameMap = brands.stream()
                .collect(Collectors.toMap(Brand::getId, Brand::getBrandName));

        // 4. ProductCatalog 리스트를 순회하며 ProductCatalogInfo DTO로 변환
        //    이때 brandIdToNameMap을 사용하여 각 ProductCatalog의 brandName을 찾습니다.
        return productCatalogsPage.map(productCatalog -> {
            String brandName = brandIdToNameMap.getOrDefault(
                    productCatalog.getBrandId(), "Unknown Brand"); // Brand 없으면 기본값

            return ProductCatalogInfo.from(productCatalog, brandName);
        });

//            productCatalogs.stream()
//                    .map(productCatalog -> {
//                        // Map에서 brandId에 해당하는 brandName을 찾고, 없으면 "Unknown Brand" 등으로 처리
//                        String brandName = brandIdToNameMap.getOrDefault(
//                                productCatalog.getBrandId(), "Unknown Brand");
//
//                        return ProductCatalogInfo.from(productCatalog, brandName);
//                    })
//                    .collect(Collectors.toList());
//        }
    }
}
