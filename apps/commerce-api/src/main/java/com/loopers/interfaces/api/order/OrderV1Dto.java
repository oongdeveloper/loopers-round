package com.loopers.interfaces.api.order;

import org.springframework.data.domain.Sort;

import java.util.List;

public class OrderV1Dto {
    public record OrderCreateRequest(
            List<OrderItemCreateRequest> items
    ){
    }

    public record OrderItemCreateRequest(
            Long productSkuId,
            Long quantity
    ){
    }

    public record OrderSelectRequest(
            int page,
            int size,
            String sortBy,
            Sort.Direction direction
    ){
        public static OrderSelectRequest of(int page, int size){
            return new OrderSelectRequest(page, size, "updateAt", Sort.Direction.DESC);
        }

    }
}
