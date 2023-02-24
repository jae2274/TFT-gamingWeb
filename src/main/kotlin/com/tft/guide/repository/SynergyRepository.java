package com.tft.guide.repository;

import com.tft.guide.entity.Synergy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SynergyRepository extends MongoRepository<Synergy, String> {
}
