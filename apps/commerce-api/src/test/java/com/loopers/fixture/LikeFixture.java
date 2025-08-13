package com.loopers.fixture;

import com.loopers.domain.like.Like;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class LikeFixture {
    public static LikeBuilder builder(){
        return new LikeBuilder();
    }

    public static LikeBuilder persistence(){
        return new LikeBuilder()
                .id(null);
    }

    public static class LikeBuilder{
        private final InstancioApi<Like> api;

        public LikeBuilder(){
            this.api = Instancio.of(Like.class);
        }

        public LikeBuilder id(Like.LikeId id) {
            this.api.set(field(Like::getId), id);
            return this;
        }

        public Like build(){
            return this.api.create();
        }
    }
}
