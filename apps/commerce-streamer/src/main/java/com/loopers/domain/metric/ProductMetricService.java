package com.loopers.domain.metric;

import com.loopers.infrastructure.metric.ProductMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductMetricService {
    private final ProductMetricRepository productMetricRepository;

    public ProductMetricService(ProductMetricRepository productMetricRepository) {
        this.productMetricRepository = productMetricRepository;
    }
}
