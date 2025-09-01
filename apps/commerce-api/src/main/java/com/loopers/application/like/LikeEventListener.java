package com.loopers.application.like;

import com.loopers.domain.like.LikeEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class LikeEventListener {
    private final LikeFacade likeFacade;

    public LikeEventListener(LikeFacade likeFacade) {
        this.likeFacade = likeFacade;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(LikeEvent.Liked event){
        likeFacade.increseLikeCount(event.productId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(LikeEvent.UnLiked event){
        likeFacade.decreseLikeCount(event.productId());
    }
}
