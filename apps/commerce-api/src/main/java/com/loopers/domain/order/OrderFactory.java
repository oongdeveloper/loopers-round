package com.loopers.domain.order;

import com.loopers.domain.catalog.Product;
import com.loopers.domain.product.ProductSku;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderFactory {

    public static Order createOrder(
            String userId, Map<Long, Long> requestMap,
            List<ProductSku> skus, List<Product> catalogs){

        Map<Long, Product> catalogMap = catalogs.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<OrderCommand.OrderItem> items = skus.stream()
                .map(sku -> {
                    return new OrderCommand.OrderItem(
                            sku.getId(),
                            sku.getProductCatalogId(),
                            sku.getUnitPrice(),
                            requestMap.get(sku.getId()),
                            catalogMap.get(sku.getId()).getProductName(),
                            sku.getUnitPrice().multiply(BigDecimal.valueOf(requestMap.get(sku.getId())))
                    );
                })
                .collect(Collectors.toList());

        return Order.create(OrderCommand.Create.of(userId, items));
    }

    class OrderCommand {

        public record Create(
                String userId,
                List<OrderCommand.OrderItem> items
        ) {

            public static OrderCommand.Create of(String userId, List<OrderCommand.OrderItem> items){
                return new OrderCommand.Create(userId, items);
            }
        }

        public record OrderItem(
                Long productSkuId,
                Long productCatalogId,
                BigDecimal price,
                Long quantity,
                String orderLineProuductName,
                BigDecimal orderLinePrice
        ) {
        }
    }
}
