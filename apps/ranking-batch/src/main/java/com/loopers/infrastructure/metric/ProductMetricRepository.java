package com.loopers.infrastructure.metric;

import com.loopers.domain.metric.ProductMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMetricRepository extends JpaRepository<ProductMetric, ProductMetric.MetricId> {
}
