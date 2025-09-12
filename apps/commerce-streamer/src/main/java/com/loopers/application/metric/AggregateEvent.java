package com.loopers.application.metric;

import com.loopers.event.core.EventPayload;

import java.util.List;

public interface AggregateEvent extends EventPayload {
    List<Long> getProductIds();
}
