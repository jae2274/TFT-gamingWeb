package com.tft.guide.repository

import com.tft.guide.entity.Deck
import org.springframework.data.mongodb.repository.ExistsQuery
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface DeckRepository : MongoRepository<Deck, String> {
    @ExistsQuery("{ 'match_id' : ?0 }")
    fun existsByMatchId(matchId: String): Boolean
}