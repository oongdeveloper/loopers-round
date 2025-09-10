package com.loopers.application.order;

import com.loopers.domain.coupons.issued.UserCouponCommand;
import com.loopers.domain.coupons.issued.UserCouponService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderFactory;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.shared.DomainEventPublisher;
import com.loopers.domain.stock.StockService;
import com.loopers.event.producer.GlobalEventPublisher;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class OrderFacade {
    private final OrderService orderService;
    private final ProductService productService;

    private final StockService stockService;
    private final UserCouponService userCouponService;

    private final DomainEventPublisher domainEventPublisher;
    private final GlobalEventPublisher globalEventPublisher;

    public OrderFacade(OrderService orderService, ProductService productService, StockService stockService, UserCouponService userCouponService, DomainEventPublisher domainEventPublisher, GlobalEventPublisher globalEventPublisher) {
        this.orderService = orderService;
        this.productService = productService;
        this.stockService = stockService;
        this.userCouponService = userCouponService;
        this.domainEventPublisher = domainEventPublisher;
        this.globalEventPublisher = globalEventPublisher;
    }

    @Transactional
    public OrderInfo createOrder(OrderCommand.Create request){
        Map<Long, Long> requestMap = request.toMap();

        List<Product> foundProducts = productService.getProuctListBySkuIds(requestMap.keySet());
        Order order = OrderFactory.createOrder(request.userId(), requestMap, foundProducts);
        BigDecimal finalPrice = userCouponService.applyCoupon(
                UserCouponCommand.Apply.of(
                        request.userId(),
                        request.couponId(),
                        order.getOriginalTotalPrice()
                )
        );
        order.updateFinalTotalPrice(finalPrice);
        order.created();

        orderService.save(order);
//        stockService.reduceStock(requestMap);
        domainEventPublisher.publish(order.pullDomainEvents());
        globalEventPublisher.publish(OrderAppEvent.Created.from(order));
        return OrderInfo.of(order);
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

    public void completed(Long orderId){
        Order order = orderService.find(orderId);
        order.complete();
        globalEventPublisher.publish(OrderAppEvent.Completed.from(order));
    }

    public void failed(Long orderId){
        Order order = orderService.find(orderId);
        order.fail();
        domainEventPublisher.publish(order.pullDomainEvents());
        globalEventPublisher.publish(OrderAppEvent.Canceled.from(order));
    }
}
