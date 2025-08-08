package com.loopers.application.order;

import com.loopers.application.order.query.OrderInfo;
import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductCatalogService;
import com.loopers.domain.coupons.issued.CouponService;
import com.loopers.domain.coupons.issued.UserCouponCommand;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderFactory;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.point.PointService;
import com.loopers.domain.product.ProductSku;
import com.loopers.domain.product.ProductSkuService;
import com.loopers.domain.stock.StockService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class OrderFacade {
    private final OrderService orderService;
    private final ProductCatalogService productCatalogService;
    private final ProductSkuService productSkuService;

    private final PointService pointService;
    private final StockService stockService;
    private final CouponService couponService;

    public OrderFacade(OrderService orderService, ProductCatalogService productCatalogService, ProductSkuService productSkuService, PointService pointService, StockService stockService, CouponService couponService) {
        this.orderService = orderService;
        this.productCatalogService = productCatalogService;
        this.productSkuService = productSkuService;
        this.pointService = pointService;
        this.stockService = stockService;
        this.couponService = couponService;
    }

    @Transactional
    public OrderInfo.DataList createOrder(OrderCommand.Create request){
        Map<Long, Long> requestMap = request.toMap();

        try{
            stockService.decreaseStock(requestMap);

            List<ProductSku> foundSkus =  productSkuService.findByIds(requestMap.keySet());

            Set<Long> catalogIds = foundSkus.stream()
                    .map(ProductSku::getProductCatalogId)
                    .collect(Collectors.toSet());
            List<Product> foundCatalogs = productCatalogService.findByIds(catalogIds);

            Order order = OrderFactory.createOrder(request.userId(), requestMap, foundSkus, foundCatalogs);

            orderService.save(order);
            BigDecimal finalPrice = couponService.applyCoupon(
                    UserCouponCommand.Apply.of(
                            request.userId(),
                            request.couponId(),
                            order.getOriginalTotalPrice()
                    )
            );
            order.updateFinalTotalPrice(finalPrice);
            order.created();
            return OrderInfo.DataList.of(order);
        } catch (RuntimeException e) {
            CompletableFuture.runAsync(
                    () -> stockService.increaseStock(requestMap)
            );
            throw new RuntimeException(e);
        }
    }
}
