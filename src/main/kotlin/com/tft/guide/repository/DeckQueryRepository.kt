package com.tft.guide.repository

import com.querydsl.core.BooleanBuilder
import com.tft.guide.controller.request.WinnersRequest
import com.tft.guide.entity.Deck
import com.tft.guide.entity.GameType
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

        var mongoDBQuery = springDataMongodbQuery

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

        val replaceCondition = BooleanBuilder()
        request.replaceMatchIds?.takeIf { it.isNotEmpty() }?.run {
            this.forEach {
                replaceCondition.and(QDeck.deck.match_id.ne(it))
            }
        }

        return mongoDBQuery
                .where(
                        QDeck.deck.placement.eq(1),
                        QDeck.deck.info.tft_game_type.eq(GameType.standard),
                        replaceCondition
                )
                .limit(request.replaceMatchIds?.let { 1 } ?: 5)
                .orderBy(QDeck.deck.info.game_datetime.desc())
                .fetch()
    }
}