package com.tft.guide.repository

import com.tft.guide.entity.Champion
import com.tft.guide.entity.Deck
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface ChampionRepository : MongoRepository<Champion, String> {
    fun findAllBySeason(season: String): List<Champion>
}