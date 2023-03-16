package com.tft.guide.repository

import com.tft.guide.entity.TftStats
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface TFTStatsRepository : MongoRepository<TftStats, String> {
    fun findTopByOrderByGameVersionDesc(): TftStats
}