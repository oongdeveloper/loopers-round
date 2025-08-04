package com.loopers.application.order;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderItem;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderInfo {
    public record OrderResponseInfo(
            Long orderId,
            String userId,
            BigDecimal totalOrderPrice,
            String status,
            ZonedDateTime orderedAt
    ){

        public static OrderResponseInfo from(Order order){
            return new OrderResponseInfo(
                    order.getId(),
                    order.getUserId(),
                    order.getTotalOrderPrice(),
                    order.getStatus(),
                    order.getCreatedAt()
            );
        }
    }

    public record OrderDetailInfo(
            Long orderId,
            String userId,
            BigDecimal totalOrderPrice,
            String status,
            ZonedDateTime orderedAt,
            List<OrderItemInfo> items
    ){
        public static OrderDetailInfo from(Order order){
            return new OrderDetailInfo(
                    order.getId(),
                    order.getUserId(),
                    order.getTotalOrderPrice(),
                    order.getStatus(),
                    order.getCreatedAt(),
                    order.getOrderItems().stream()
                            .map(OrderItemInfo::from)
                            .collect(Collectors.toList())
            );
        }
    }

    public record OrderItemInfo(
            Long orderItemId,
            Long productSkuId,
            Long productCatalogId,
            Long quantity,
            BigDecimal totalItemPrice,
            String productName,
            BigDecimal unitPrice
    ){
        public static OrderItemInfo from(OrderItem item) {
            return new OrderItemInfo(
                    item.getId(),
                    item.getProductSkuId(),
                    item.getProductCatalogId(),
                    item.getQuantity(),
                    item.getTotalItemPrice(),
                    item.getOrderItemProductName(),
                    item.getOrderItemUnitPrice()
            );
        }
    }
}
