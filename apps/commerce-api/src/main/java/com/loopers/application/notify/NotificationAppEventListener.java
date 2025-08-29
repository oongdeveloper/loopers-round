package com.loopers.application.notify;

import com.loopers.support.event.AppErrorEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationAppEventListener {

    @Async
    @EventListener
    public void handle(AppErrorEvent event){
        log.error("Applciation Exception Event 발생 {}, ", event);
    }
}
