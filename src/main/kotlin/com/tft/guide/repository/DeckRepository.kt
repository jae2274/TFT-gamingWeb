package com.tft.guide.repository


import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.tft.guide.controller.request.WinnersRequest
import com.tft.guide.entity.Deck
import com.tft.guide.entity.QDeck
import com.tft.guide.entity.QDeck_Unit
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.support.SpringDataMongodbQuery
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface DeckRepository : MongoRepository<Deck, String>, QuerydslPredicateExecutor<Deck> {
//    fun findByCharacterId(placement: Int, units: List<WinnersRequest.Unit>): List<Deck> {
//        val springDataMongodbQuery = SpringDataMongodbQuery(mongoTemplate, Deck::class.java)
//
//
//        val decks = springDataMongodbQuery.where()
//                .anyEmbedded(QDeck.deck.units, QDeck_Unit.unit)
//                .on(
//                        QDeck_Unit.unit.character_id.eq("TFT8_Zed").and(QDeck_Unit.unit.tier.eq(3))
//                )
//                .anyEmbedded(QDeck.deck.units, QDeck_Unit.unit)
//                .on(
//                        QDeck_Unit.unit.character_id.eq("TFT8_Nilah").and(QDeck_Unit.unit.tier.eq(3))
//                )
//                .limit(5)
//
//                .fetch()
//
//        for (deck in decks) {
//            println(deck.units)
//        }
//
//
//    }
}