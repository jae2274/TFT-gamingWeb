package com.tft.guide.repository;

import com.tft.guide.entity.IdSet
import com.tft.guide.entity.IdType
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface IdSetRepository : MongoRepository<IdSet, String> {
    @Query("{'season': ?0, seasonNumber: ?1, type: ?2}")
    fun findBySeasonAndType(season: String, seasonNumber: Int, type: IdType): IdSet?
}