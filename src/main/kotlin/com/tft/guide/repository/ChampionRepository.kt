package com.tft.guide.repository

import com.tft.guide.entity.Champion
import org.springframework.data.mongodb.repository.MongoRepository

interface ChampionRepository : MongoRepository<Champion, String> {
    fun findAllBySeason(season: String): List<Champion>
}