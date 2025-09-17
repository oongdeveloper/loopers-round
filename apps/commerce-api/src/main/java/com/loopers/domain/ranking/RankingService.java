package com.loopers.domain.ranking;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class RankingService {
    private final RankingRepository repository;

    public RankingService(RankingRepository repository) {
        this.repository = repository;
    }


    public Set<Long> getRange(String key, int start, int end){
        return repository.getRange(key, start, end);
    }

    public Map<Long, Float> getRangeWithScore(String key, int start, int end){
        return repository.getRangeWithScore(key, start, end);
    }

    public void add(String key, Map<Long, Float> map){
        repository.add(key, map);
    }

    public Long count(String key){
        return repository.count(key);
    }

}
