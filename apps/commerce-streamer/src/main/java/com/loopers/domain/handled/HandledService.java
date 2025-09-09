package com.loopers.domain.handled;

import com.loopers.infrastructure.handled.HandledRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HandledService {
    private final HandledRepository handledRepository;

    public HandledService(HandledRepository handledRepository) {
        this.handledRepository = handledRepository;
    }

    public Optional<Handled> findByEventId(String eventId){
        return handledRepository.findByEventId(eventId);
    }
}
