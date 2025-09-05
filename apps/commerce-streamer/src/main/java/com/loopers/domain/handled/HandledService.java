package com.loopers.domain.handled;

import com.loopers.infrastructure.handled.HandledRepository;
import org.springframework.stereotype.Service;

@Service
public class HandledService {
    private final HandledRepository handledRepository;

    public HandledService(HandledRepository handledRepository) {
        this.handledRepository = handledRepository;
    }
}
