package com.tft.guide.repository

import com.querydsl.core.BooleanBuilder
import com.tft.guide.controller.request.WinnersReq
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
    fun findByCharacterId(placement: Int, request: WinnersReq): List<Deck> {
        val springDataMongodbQuery = SpringDataMongodbQuery(mongoTemplate, Deck::class.java)

        var mongoDBQuery = springDataMongodbQuery

        for (champion in request.champions) {
            mongoDBQuery = mongoDBQuery
                    .anyEmbedded(QDeck.deck.units, QDeck_Unit.unit)
                    .on(
                            QDeck_Unit.unit.character_id.eq(champion.dataId)
                                    .and(QDeck_Unit.unit.tier.goe(champion.tier))
                                    .and(
                                            champion.itemCount.takeIf { it > 0 }
                                                    ?.let { QDeck_Unit.unit.itemNames[it - 1].isNotNull }
                                                    ?: null
                                    )
                    )
        }

        for (item in request.items) {
            mongoDBQuery = mongoDBQuery
                    .anyEmbedded(QDeck.deck.units, QDeck_Unit.unit)
                    .on(QDeck_Unit.unit.itemNames.any().eq(item.dataId))
        }

        val conditionForAugment = request.augments.fold(BooleanBuilder()) { where, augmentReq -> where.and(QDeck.deck.augments.any().eq(augmentReq.dataId)) }

        return mongoDBQuery
                .where(
                        QDeck.deck.placement.eq(1),
                        QDeck.deck.info.tft_game_type.eq(GameType.standard),
                        conditionForAugment,
                )
                .offset(request.offset)
                .limit(request.size)
                .orderBy(QDeck.deck.info.game_datetime.desc())
                .fetch()
    }
}