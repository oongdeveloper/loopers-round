package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderCommand;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    // TODO. 리팩토링
    public record CreateRequest(
       List<ItemCreateRequest> items
    ){
        public Map<Long, Long> toMap(){
            return this.items().stream()
                    .collect(Collectors.toMap(ItemCreateRequest::skuId, ItemCreateRequest::quantity));
        }

        public OrderCommand.Create toCommand(Long userId){
            return new OrderCommand.Create(
                    userId,
                    items.stream()
                            .map(item -> new OrderCommand.ItemCreate(item.skuId, item.quantity()))
                            .collect(Collectors.toList()
                            )
                            ,0L);
        }
    }

    public record ItemCreateRequest(
       Long skuId,
       Long quantity
    ){}

    public record ListResponse(
        Long orderId,
        String userId,
        BigDecimal totalOrderPrice,
        String status,
        ZonedDateTime orderedAt
    ){
//        public static OrderV1Dto.ListResponse from(OrderQuery.ListQuery list) {
//            return new ListResponse(
//                    list.orderId(),
//                    list.userId(),
//                    list.totalOrderPrice(),
//                    list.status(),
//                    list.orderedAt()
//            );
//        }
    }

    public record DetailResponse(
            Long orderId,
            String userId,
            BigDecimal totalOrderPrice,
            String status,
            ZonedDateTime orderedAt,
            List<ItemDetailResponse> items
    ){
//        public static OrderV1Dto.DetailResponse from(OrderQuery.DetailQuery detail){
//            return new DetailResponse(
//                    detail.orderId(),
//                    detail.userId(),
//                    detail.totalOrderPrice(),
//                    detail.status(),
//                    detail.orderedAt(),
//                    ItemDetailResponse.from(detail.items())
//            );
//        }
    }


    public record ItemDetailResponse(
            Long orderLineId,
            Long productSkuId,
            Long productCatalogId,
            Long quantity,
            BigDecimal totalItemPrice,
            String productName,
            String brandName,
            BigDecimal unitPrice
    ){
//        public static OrderV1Dto.ItemDetailResponse from(OrderQuery.ItemDetailQuery item) {
//            return new ItemDetailResponse(
//                    item.orderItemId(),
//                    item.productSkuId(),
//                    item.productCatalogId(),
//                    item.quantity(),
//                    item.totalItemPrice(),
//                    item.productName(),
//                    item.brandName(),
//                    item.unitPrice()
//            );
//        }

//        public static List<OrderV1Dto.ItemDetailResponse> from(List<OrderQuery.ItemDetailQuery> items) {
//            return items.stream()
//                    .map(OrderV1Dto.ItemDetailResponse::from)
//                    .collect(Collectors.toList());
//        }
    }
}
