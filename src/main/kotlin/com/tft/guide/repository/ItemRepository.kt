package com.tft.guide.repository

import com.tft.guide.entity.Item
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface ItemRepository : MongoRepository<Item, String> {
    fun findAllBySeason(season: String): List<Item>
}