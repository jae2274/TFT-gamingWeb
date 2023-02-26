package com.tft.guide.repository

import com.tft.guide.entity.Item
import com.tft.guide.entity.Synergy
import org.springframework.data.mongodb.repository.MongoRepository

interface SynergyRepository : MongoRepository<Synergy, String> {
    fun findAllBySeason(season: String): List<Synergy>
}