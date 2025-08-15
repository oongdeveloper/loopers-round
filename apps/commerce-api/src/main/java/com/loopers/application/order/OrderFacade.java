package com.loopers.application.order;

import com.loopers.domain.coupons.issued.UserCouponCommand;
import com.loopers.domain.coupons.issued.UserCouponService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderFactory;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.stock.StockService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderFacade {
    private final OrderService orderService;
    private final ProductService productService;

    private final StockService stockService;
    private final UserCouponService userCouponService;

    public OrderFacade(OrderService orderService, ProductService productService, StockService stockService, UserCouponService userCouponService) {
        this.orderService = orderService;
        this.productService = productService;
        this.stockService = stockService;
        this.userCouponService = userCouponService;
    }

    @Transactional
    public OrderInfo createOrder(OrderCommand.Create request){
        Map<Long, Long> requestMap = request.toMap();

        try{
            stockService.decreaseStock(requestMap);
            List<Product> foundProducts = productService.getProuctListByIds(requestMap.keySet());

//            List<ProductSku> foundSkus =  productSkuService.findByIds(requestMap.keySet());
//            Set<Long> catalogIds = foundSkus.stream()
//                    .map(ProductSku::getProductCatalogId)
//                    .collect(Collectors.toSet());
//            List<Product> foundCatalogs = productCatalogService.findByIds(catalogIds);
//            Order order = OrderFactory.createOrder(request.userId(), requestMap, foundSkus, foundCatalogs);
            Order order = OrderFactory.createOrder(request.userId(), requestMap, foundProducts);

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

    public Page<OrderResult.DataList> getOrderList(OrderQuery.Summary query) {
        return orderService.getOrderList(query.userId(), query.pageable())
                .map(OrderResult.DataList::of)
                ;
    }

    public OrderResult.DataDetail getOrderDetail(OrderQuery.Detail query) {
        Order order = orderService.getOrderDetail(query.orderId());
        return OrderResult.DataDetail.of(order.getId(), order.getLines().getLines());
    }
}
