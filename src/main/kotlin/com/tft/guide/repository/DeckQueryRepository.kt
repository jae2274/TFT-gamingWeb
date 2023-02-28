package com.tft.guide.repository

import com.tft.guide.controller.request.WinnersRequest
import com.tft.guide.entity.Deck
import com.tft.guide.entity.QDeck
import com.tft.guide.entity.QDeck_Unit
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.support.SpringDataMongodbQuery
import org.springframework.stereotype.Repository

@Repository
class DeckQueryRepository(
        val deckRepository: DeckRepository,
        val mongoTemplate: MongoTemplate,
) {
    fun findByCharacterId(placement: Int, request: WinnersRequest): List<Deck> {
        val springDataMongodbQuery = SpringDataMongodbQuery(mongoTemplate, Deck::class.java)

        var mongoDBQuery = springDataMongodbQuery.where(QDeck.deck.placement.eq(1))

        for (champion in request.champions) {
            mongoDBQuery = mongoDBQuery
                    .anyEmbedded(QDeck.deck.units, QDeck_Unit.unit)
                    .on(QDeck_Unit.unit.character_id.eq(champion.dataId).and(QDeck_Unit.unit.tier.goe(champion.tier)))
        }

        for (item in request.items) {
            mongoDBQuery = mongoDBQuery
                    .anyEmbedded(QDeck.deck.units, QDeck_Unit.unit)
                    .on(QDeck_Unit.unit.itemNames.any().eq(item.dataId))
        }

        return mongoDBQuery
                .limit(5)
                .orderBy(QDeck.deck.info.game_datetime.desc())
                .fetch()
    }
}