package com.loopers.application.order;

import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductCatalogService;
import com.loopers.domain.coupons.issued.UserCouponService;
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
    private final UserCouponService userCouponService;

    public OrderFacade(OrderService orderService, ProductCatalogService productCatalogService, ProductSkuService productSkuService, PointService pointService, StockService stockService, UserCouponService userCouponService) {
        this.orderService = orderService;
        this.productCatalogService = productCatalogService;
        this.productSkuService = productSkuService;
        this.pointService = pointService;
        this.stockService = stockService;
        this.userCouponService = userCouponService;
    }

    @Transactional
    public OrderInfo createOrder(OrderCommand.Create request){
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
            BigDecimal finalPrice = userCouponService.applyCoupon(
                    UserCouponCommand.Apply.of(
                            request.userId(),
                            request.couponId(),
                            order.getOriginalTotalPrice()
                    )
            );
            order.updateFinalTotalPrice(finalPrice);
            order.created();
            return OrderInfo.of(order);
        } catch (RuntimeException e) {
            CompletableFuture.runAsync(
                    () -> stockService.restoreStock(requestMap)
            );
            throw new RuntimeException(e);
        }
    }
}
