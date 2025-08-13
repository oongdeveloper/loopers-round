package com.loopers.application.order;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderLine;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResult {
    public record DataList(
            Long id,
            BigDecimal finalTotalPrice,
            BigDecimal originalTotalPrice,
            String status
    ){
        public static DataList of(Order order){
            return new DataList(order.getId(), order.getFinalTotalPrice(), order.getOriginalTotalPrice(), order.getStatus());
        }
    }

    public record DataDetail(
            Long orderId,
            List<LineDetail> lines
    ){
        public static DataDetail of(Long orderId, List<OrderLine> lines){
            return new DataDetail(
                    orderId,
                    lines.stream()
                            .map(line -> new LineDetail(
                                    line.getId(),
                                    line.getProductSkuId(),
                                    line.getQuantity(),
                                    line.getTotalLinePrice(),
                                    line.getOrderLineProductName(),
                                    line.getOrderLinePrice()
                            ))
                            .collect(Collectors.toList())
            );
        }
    }

    public record LineDetail(
            Long id,
            Long productSkuId,
            Long quantity,
            BigDecimal totalLinePrice,
            String orderLineProductName,
            BigDecimal orderLinePrice
    ){}
}
