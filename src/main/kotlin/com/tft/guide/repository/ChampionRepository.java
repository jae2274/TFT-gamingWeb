package com.tft.guide.repository;

import com.tft.guide.entity.Champion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ChampionRepository extends MongoRepository<Champion, String>
        , QuerydslPredicateExecutor<Champion> {
}
