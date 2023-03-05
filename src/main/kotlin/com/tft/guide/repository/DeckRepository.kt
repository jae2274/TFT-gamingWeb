package com.tft.guide.repository


import com.tft.guide.entity.Deck
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface DeckRepository : MongoRepository<Deck, String>, QuerydslPredicateExecutor<Deck>