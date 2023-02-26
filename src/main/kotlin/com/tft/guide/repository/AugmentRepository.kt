package com.tft.guide.repository

import com.tft.guide.entity.Augment
import org.springframework.data.mongodb.repository.MongoRepository

interface AugmentRepository : MongoRepository<Augment, String> {
    fun findAllBySeason(season: String): List<Augment>
}