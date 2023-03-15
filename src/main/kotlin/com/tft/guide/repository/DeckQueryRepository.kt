package com.tft.guide.repository

import com.querydsl.core.BooleanBuilder
import com.tft.guide.controller.request.WinnersReq
import com.tft.guide.entity.WinnerDeck
import com.tft.guide.entity.GameType
import com.tft.guide.entity.QWinnerDeck
import com.tft.guide.entity.QWinnerDeck_Trait
import com.tft.guide.entity.QWinnerDeck_Unit
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.support.SpringDataMongodbQuery
import org.springframework.stereotype.Repository

@Repository
class DeckQueryRepository(
        val winnerDeckRepository: WinnerDeckRepository,
        val mongoTemplate: MongoTemplate,
) {
    fun findByCharacterId(request: WinnersReq): List<WinnerDeck> {
        val springDataMongodbQuery = SpringDataMongodbQuery(mongoTemplate, WinnerDeck::class.java)

        var mongoDBQuery = springDataMongodbQuery

        for (champion in request.champions) {
            mongoDBQuery = mongoDBQuery
                    .anyEmbedded(QWinnerDeck.winnerDeck.units, QWinnerDeck_Unit.unit)
                    .on(
                            QWinnerDeck_Unit.unit.character_id.eq(champion.dataId)
                                    .and(QWinnerDeck_Unit.unit.tier.goe(champion.tier))
                                    .and(
                                            champion.itemCount.takeIf { it > 0 }
                                                    ?.let { QWinnerDeck_Unit.unit.itemNames[it - 1].isNotNull }
                                                    ?: null
                                    )
                    )
        }

        for (item in request.items) {
            mongoDBQuery = mongoDBQuery
                    .anyEmbedded(QWinnerDeck.winnerDeck.units, QWinnerDeck_Unit.unit)
                    .on(QWinnerDeck_Unit.unit.itemNames.any().eq(item.dataId))
        }

        for (synergy in request.synergies) {
            mongoDBQuery = mongoDBQuery
                    .anyEmbedded(QWinnerDeck.winnerDeck.traits, QWinnerDeck_Trait.trait)
                    .on(
                            QWinnerDeck_Trait.trait.name.eq(synergy.dataId)
                                    .and(QWinnerDeck_Trait.trait.tier_current.goe(synergy.tier))
                    )
        }

        val conditionForAugment = request.augments.fold(BooleanBuilder()) { where, augmentReq -> where.and(QWinnerDeck.winnerDeck.augments.any().eq(augmentReq.dataId)) }

        return mongoDBQuery
                .where(
                        QWinnerDeck.winnerDeck.info.tft_game_type.eq(GameType.standard),
                        conditionForAugment,
                )
                .offset(request.offset)
                .limit(request.size)
                .orderBy(QWinnerDeck.winnerDeck.info.game_datetime.desc())
                .fetch()
    }
}