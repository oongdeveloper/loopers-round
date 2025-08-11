package com.loopers.domain.order;

import com.loopers.domain.catalog.Product;
import com.loopers.domain.product.ProductSku;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderFactory {

    public static Order createOrder(
            Long userId, Map<Long, Long> requestMap,
            List<ProductSku> skus, List<Product> catalogs){

        Map<Long, Product> catalogMap = catalogs.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        Order order = Order.create(userId);
        skus.forEach(sku -> {
            order.addOrderLine(OrderLine.create(
                    sku.getId(),
                    requestMap.get(sku.getId()),
                    catalogMap.get(sku.getId()).getProductName(),
                    sku.getUnitPrice()
            ));
        });
        return order;
    }
}
