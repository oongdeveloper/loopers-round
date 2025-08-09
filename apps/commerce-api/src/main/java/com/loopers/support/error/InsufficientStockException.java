package com.loopers.support.error;

import java.util.List;

public class InsufficientStockException extends RuntimeException{

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(List<Long> failedSkuIds) {
        super("재고가 부족한 상품이 있습니다: " + failedSkuIds);
    }

}
