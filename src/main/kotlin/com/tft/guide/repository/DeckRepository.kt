package com.tft.guide.repository

import com.tft.guide.entity.Deck
import org.springframework.data.mongodb.repository.MongoRepository

interface DeckRepository : MongoRepository<Deck, String>