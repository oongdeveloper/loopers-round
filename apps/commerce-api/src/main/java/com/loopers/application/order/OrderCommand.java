package com.loopers.application.order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderCommand {
    public record Create(
            Long userId,
            List<ItemCreate> items,
            Long couponId
    ){
            public Map<Long, Long> toMap(){
                return this.items.stream()
                        .collect(Collectors.toMap(ItemCreate::skuId, ItemCreate::quantity));
            }
    }

    public record ItemCreate(
            Long skuId,
            Long quantity
    ){}

}
