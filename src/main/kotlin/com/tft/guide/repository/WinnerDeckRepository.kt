package com.tft.guide.repository


import com.tft.guide.entity.WinnerDeck
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface WinnerDeckRepository : MongoRepository<WinnerDeck, String>, QuerydslPredicateExecutor<WinnerDeck>