package com.loopers.domain.ranking;

import com.loopers.domain.BaseAuditableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ranking_weight")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RankingWeight extends BaseAuditableEntity {
    @Id
    String name;
    Float weight;
}
