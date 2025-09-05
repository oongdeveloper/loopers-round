package com.loopers.event.producer.domain;

import com.loopers.event.core.EventEnvelop;
import com.loopers.event.producer.infrastructure.DeadEventRepository;
import org.springframework.stereotype.Component;

@Component
public class DeadEventService {
    private final DeadEventRepository repository;

    public DeadEventService(DeadEventRepository repository) {
        this.repository = repository;
    }

    public void save(EventEnvelop<?> event, String errMsg){
        repository.save(DeadEvent.from(event, errMsg));
    }
}
