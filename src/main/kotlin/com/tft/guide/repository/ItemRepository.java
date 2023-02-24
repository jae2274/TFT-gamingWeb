package com.tft.guide.repository;

import com.tft.guide.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ItemRepository extends MongoRepository<Item, String>
        , QuerydslPredicateExecutor<Item> {
}
