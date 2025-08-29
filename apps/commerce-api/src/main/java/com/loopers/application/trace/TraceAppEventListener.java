package com.loopers.application.trace;

import com.loopers.support.event.AppEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TraceAppEventListener {

    @Async
    @EventListener
    public void handle(AppEvent event){
        log.info("Local Application Event 발행 {}", event);
    }
}
