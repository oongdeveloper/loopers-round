package com.loopers.domain.ranking;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RankingService {
    private final RankingRepository rankingRepository;

    public RankingService(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }
    public void add(String key, Map<String, Double> map){
        rankingRepository.add(key, map);
    }

    public Float getWeight(String key){
        return rankingRepository.getWeight(key);
    }
}
