package com.loopers.application.order;

import com.loopers.domain.catalog.ProductCatalog;
import com.loopers.domain.catalog.ProductCatalogService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderFactory;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.point.DeductPointCommand;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointService;
import com.loopers.domain.product.ProductSku;
import com.loopers.domain.product.ProductSkuService;
import com.loopers.domain.stock.Stock;
import com.loopers.domain.stock.StockService;
import com.loopers.interfaces.api.order.OrderV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderFacade {
    private final OrderService orderService;
    private final ProductCatalogService productCatalogService;
    private final ProductSkuService productSkuService;

    private final PointService pointService;
    private final StockService stockService;

    public OrderFacade(OrderService orderService, ProductCatalogService productCatalogService, ProductSkuService productSkuService, PointService pointService, StockService stockService) {
        this.orderService = orderService;
        this.productCatalogService = productCatalogService;
        this.productSkuService = productSkuService;
        this.pointService = pointService;
        this.stockService = stockService;
    }

    @Transactional
    public OrderInfo.OrderDetailInfo createOrder(String userId, OrderV1Dto.OrderCreateRequest request) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "주문할 상품 항목이 없습니다.");
        }

        Set<Long> requestedSkuIds = request.items().stream()
                .map(command -> command.productSkuId())
                .collect(Collectors.toSet());

        List<ProductSku> foundSkus = productSkuService.findByIds(requestedSkuIds);
        Map<Long, ProductSku> skuMap = foundSkus.stream()
                .collect(Collectors.toMap(ProductSku::getId, Function.identity()));

        Set<Long> catalogIds = foundSkus.stream()
                .map(ProductSku::getProductCatalogId)
                .collect(Collectors.toSet());

        List<ProductCatalog> foundCatalogs = productCatalogService.findByIds(catalogIds);
        Map<Long, ProductCatalog> catalogMap = foundCatalogs.stream()
                .collect(Collectors.toMap(ProductCatalog::getId, Function.identity()));

        List<OrderCreateCommand.OrderItemCreateCommand> orderItemsToCreate = request.items().stream()
                .map(reqItem -> {
                    ProductSku productSku = skuMap.get(reqItem.productSkuId());
                    if (productSku == null) {
                        throw new CoreException(ErrorType.BAD_REQUEST, "Product SKU를 찾을 수 없습니다: " + reqItem.productSkuId());
                    }

                    if (productSku.getStatus() != ProductSku.SkuStatus.AVAILABLE){
                        throw new CoreException(ErrorType.CONFLICT, "상품이 판매 중 상태가 아닙니다. " + reqItem.productSkuId());
                    }

                    ProductCatalog productCatalog = catalogMap.get(productSku.getProductCatalogId());
                    if (productCatalog == null) {
                        throw new CoreException(ErrorType.BAD_REQUEST,"Product Catalog를 찾을 수 없습니다: " + productSku.getProductCatalogId());
                    }

                    // 재고 차감
                    stockService.decreaseStock(reqItem.productSkuId(), reqItem.quantity());

                    return new OrderCreateCommand.OrderItemCreateCommand(
                            reqItem.productSkuId(),
                            productSku.getProductCatalogId(),
                            reqItem.quantity(),
                            productSku.getUnitPrice(),
                            productCatalog.getProductName()
                    );
                })
                .collect(Collectors.toList());

        OrderCreateCommand orderCreateRequest = new OrderCreateCommand(
                userId,
                orderItemsToCreate
        );

        Order savedOrder = orderService.createOrder(orderCreateRequest);
        // 포인트 차감
        pointService.deduct(DeductPointCommand.of(savedOrder.getUserId(), savedOrder.getTotalOrderPrice()));
        savedOrder.updateStatus("COMPLETED");
        return OrderInfo.OrderDetailInfo.from(savedOrder);
    }

    public Page<OrderInfo.OrderResponseInfo> getOrderList(Long userId, OrderV1Dto.OrderSelectRequest request) {
        Sort sort = Sort.by(Sort.Direction.fromString(request.direction().name()), request.sortBy());
        Pageable pageable = PageRequest.of(request.page(), request.size(), sort);
        return orderService.getOrderList(userId, pageable);
    }

    public OrderInfo.OrderDetailInfo getOrderDetail(Long orderId) {
        return orderService.getOrderDetail(orderId);
    }


    public void createOrder(String userId, OrderV1Dto.CreateRequest request){
        Map<Long, Long> requestMap = request.toMap();
        List<ProductSku> foundSkus =  productSkuService.findByIds(requestMap.keySet());

        Set<Long> catalogIds = foundSkus.stream()
                .map(ProductSku::getProductCatalogId)
                .collect(Collectors.toSet());
        List<ProductCatalog> foundCatalogs = productCatalogService.findByIds(catalogIds);

        Order order = OrderFactory.createOrder(userId, requestMap, foundSkus, foundCatalogs);
        // TODO. INSERT 가 여러 번 요청됨.
        orderService.save(order);

        // 재고 정보 조회
        List<Stock> stocks = stockService.findBySkuIds(requestMap.keySet());
        stockService.decreaseStock(stocks, requestMap);

        // Point 정보 조회
        Point point = pointService.find(userId).get();
        point.deduct(order.getTotalOrderPrice());
    }
}
