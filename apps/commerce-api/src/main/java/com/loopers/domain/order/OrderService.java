package com.loopers.domain.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(Order order){
        orderRepository.save(order);
    }

    public Order find(Long orderId){
        return orderRepository.findByIdWithOrderLines(orderId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 주문입니다."));
    }

    public Map<Long, Long> getOrderedProductQuantity(Long orderId){
        return orderRepository.findByIdWithOrderLines(orderId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 주문입니다."))
                .getLines().getLines().stream()
                .collect(Collectors.toMap(OrderLine::getProductSkuId, OrderLine::getQuantity));
    }
}
