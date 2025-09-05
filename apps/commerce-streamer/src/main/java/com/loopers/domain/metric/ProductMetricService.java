package com.loopers.domain.metric;

import com.loopers.infrastructure.metric.ProductMetricRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ProductMetricService {
    private final ProductMetricRepository productMetricRepository;

    public ProductMetricService(ProductMetricRepository productMetricRepository) {
        this.productMetricRepository = productMetricRepository;
    }

    public ProductMetric findById(Long productId, LocalDate date){
        Optional<ProductMetric> opProductMetric = productMetricRepository.findById(ProductMetric.MetricId.of(productId, date));
        if(opProductMetric.isPresent()) return opProductMetric.get();

        return productMetricRepository.save(ProductMetric.of(productId, date));
    }
}
