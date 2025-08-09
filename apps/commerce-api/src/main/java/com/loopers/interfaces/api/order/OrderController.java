package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderFacade;
import com.loopers.interfaces.api.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController implements OrderV1ApiSpec{
    private final OrderFacade orderFacade;

    public OrderController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @Override
    @PostMapping("/api/v1/orders")
    public ApiResponse<?> create(@RequestHeader(value = "X-USER-ID") Long userId, @RequestBody OrderV1Dto.CreateRequest request) {
        orderFacade.createOrder(request.toCommand(userId));
        return ApiResponse.success();

    }

    @Override
    @GetMapping("/api/v1/orders")
    public ApiResponse<?> getList(@RequestHeader(value = "X-USER-ID") String userId) {
        return null;
    }

    @Override
    @GetMapping("/api/v1/order/{orderId}")
    public ApiResponse<?> getDetail(@RequestHeader(value = "X-USER-ID") String userId, @PathVariable("orderId") Long orderId) {
        return null;
    }


}
