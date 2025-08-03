package com.loopers.domain.order;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLines {

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    List<OrderLine> lines;

    private OrderLines(List<OrderLine> lines){
        this.lines = new ArrayList<>(lines);
    }

    public static OrderLines empty(){
        return new OrderLines(new ArrayList<>());
    }

    void add(OrderLine orderLine){
        validateDuplicateProduct(orderLine);
        this.lines.add(orderLine);
    }

    void validateDuplicateProduct(OrderLine orderLine) {
        boolean exist = lines.stream().anyMatch(line -> line.hasSameProduct(orderLine));
        if (exist) {
            throw new IllegalArgumentException("동일한 상품 목록이 주문에 존재합니다." + orderLine.productSkuId);
        }
    }
}
