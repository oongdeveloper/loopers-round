package com.loopers.domain.order;


import com.loopers.domain.product.Product;

import java.util.List;
import java.util.Map;

public class OrderFactory {

    public static Order createOrder(
            Long userId, Map<Long, Long> requestMap,
            List<Product> products
//            List<ProductSku> skus, List<Product> catalogs
    ){

//        Map<Long, Product> catalogMap = catalogs.stream()
//                .collect(Collectors.toMap(Product::getId, Function.identity()));

        Order order = Order.create(userId);
        products.forEach(product -> {
            product.getSkus().forEach(sku -> {
                order.addOrderLine(OrderLine.create(
                        sku.getId(),
                        requestMap.get(sku.getId()),
                        product.getProductName(),
                        sku.getUnitPrice()
                ));
            });
        });
//        skus.forEach(sku -> {
//            order.addOrderLine(OrderLine.create(
//                    sku.getId(),
//                    requestMap.get(sku.getId()),
//                    catalogMap.get(sku.getId()).getProductName(),
//                    sku.getUnitPrice()
//            ));
//        });
        return order;
    }
}
