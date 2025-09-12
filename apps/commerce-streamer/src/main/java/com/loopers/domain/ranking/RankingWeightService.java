package com.loopers.domain.ranking;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingWeightService {
    private final RankingWeightRepository rankingWeightRepository;

    public RankingWeightService(RankingWeightRepository rankingWeightRepository) {
        this.rankingWeightRepository = rankingWeightRepository;
    }

    public List<RankingWeight> getWeights(){
        return rankingWeightRepository.getWeights();
    }

    public void addWeight(String key, Float weight){
        rankingWeightRepository.addWeight(key, weight);
    }
}
