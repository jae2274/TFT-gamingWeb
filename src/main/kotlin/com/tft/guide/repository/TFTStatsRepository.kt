package com.tft.guide.repository

import com.tft.guide.entity.TftStats
import org.springframework.data.mongodb.repository.MongoRepository

interface TFTStatsRepository : MongoRepository<TftStats, String> {
    fun findBySeasonAndSeasonNumberAndGameVersion(season: String, seasonNumber: Int, gameVersion: String): TftStats?
    fun findTopByOrderByGameVersionDesc(): TftStats
    fun findTopBySeasonOrderByCreatedAtDesc(season: String): TftStats
}