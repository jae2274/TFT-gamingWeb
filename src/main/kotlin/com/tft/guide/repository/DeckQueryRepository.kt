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
        val mongoTemplate: MongoTemplate,
) {
    fun findWinnerDecks(request: WinnersReq): List<WinnerDeck> {
        var mongoDBQuery = SpringDataMongodbQuery(mongoTemplate, WinnerDeck::class.java)

        mongoDBQuery = buildChampionsQuery(mongoDBQuery, request.champions)
                .let { buildItemsQuery(it, request.items) }
                .let { buildSynergiesQuery(it, request.synergies) }


        val conditionForAugment = buildAugmentsBooleanBuilder(request.augments)

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

    private fun buildChampionsQuery(mongoDBQuery: SpringDataMongodbQuery<WinnerDeck>, championReqs: List<WinnersReq.ChampionReq>): SpringDataMongodbQuery<WinnerDeck> {
        return championReqs.fold(mongoDBQuery) { championMongoDBQuery, championReq ->
            championMongoDBQuery
                    .anyEmbedded(QWinnerDeck.winnerDeck.units, QWinnerDeck_Unit.unit)
                    .on(
                            QWinnerDeck_Unit.unit.character_id.eq(championReq.dataId)
                                    .and(QWinnerDeck_Unit.unit.tier.goe(championReq.tier))
                                    .and(
                                            championReq.itemCount.takeIf { it > 0 }
                                                    ?.let { QWinnerDeck_Unit.unit.itemNames[it - 1].isNotNull }
                                                    ?: null
                                    )
                    )
        }
    }

    private fun buildItemsQuery(mongoDBQuery: SpringDataMongodbQuery<WinnerDeck>, itemReqs: List<WinnersReq.ItemReq>): SpringDataMongodbQuery<WinnerDeck> {
        return itemReqs.fold(mongoDBQuery) { itemMongoDBQuery, itemReq ->
            itemMongoDBQuery
                    .anyEmbedded(QWinnerDeck.winnerDeck.units, QWinnerDeck_Unit.unit)
                    .on(QWinnerDeck_Unit.unit.itemNames.any().eq(itemReq.dataId))
        }
    }

    private fun buildSynergiesQuery(mongoDBQuery: SpringDataMongodbQuery<WinnerDeck>, synergyReqs: List<WinnersReq.SynergyReq>): SpringDataMongodbQuery<WinnerDeck> {
        return synergyReqs.fold(mongoDBQuery) { synergyMongoDBQuery, synergyReq ->
            synergyMongoDBQuery
                    .anyEmbedded(QWinnerDeck.winnerDeck.traits, QWinnerDeck_Trait.trait)
                    .on(
                            QWinnerDeck_Trait.trait.name.eq(synergyReq.dataId)
                                    .and(QWinnerDeck_Trait.trait.tier_current.goe(synergyReq.tier))
                    )
        }
    }

    private fun buildAugmentsBooleanBuilder(augmentReqs: List<WinnersReq.AugmentReq>): BooleanBuilder {
        return augmentReqs.fold(BooleanBuilder()) { where, augmentReq -> where.and(QWinnerDeck.winnerDeck.augments.any().eq(augmentReq.dataId)) }
    }
}