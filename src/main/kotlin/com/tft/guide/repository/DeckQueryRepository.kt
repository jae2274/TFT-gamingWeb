package com.tft.guide.repository

import com.jyoliar.mongo.and
import com.jyoliar.mongo.eq
import com.jyoliar.mongo.greaterEqual
import com.tft.guide.controller.request.WinnersReq
import com.tft.guide.entity.WinnerDeck
import org.litote.kmongo.pos
import org.springframework.data.domain.Sort
import org.springframework.data.mapping.toDotPath
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.*
import org.springframework.stereotype.Repository

@Repository
class DeckQueryRepository(
    val mongoOperations: MongoOperations,
) {
    fun findWinnerDecks(request: WinnersReq): List<WinnerDeck> =
        mongoOperations.find(
            Query.query(
                and(
                    buildChampionsQuery(request.champions),
                    buildItemsQuery(request.items),
                    buildSynergiesQuery(request.synergies),
                    buildAugmentsBooleanBuilder(request.augments),
                )
            )
                .with(Sort.by(Sort.Direction.DESC, (WinnerDeck::info / WinnerDeck.Info::game_datetime).toDotPath()))
                .skip(request.offset)
                .limit(request.size.toInt()),
            WinnerDeck::class.java
        )


    private fun buildChampionsQuery(championReqs: List<WinnersReq.ChampionReq>): Criteria? =
        if (championReqs.isEmpty()) null
        else
            Criteria().andOperator(
                championReqs
                    .map { championReq ->
                        WinnerDeck::units.elemMatch(
                            and(
                                WinnerDeck.Unit::character_id eq championReq.dataId,
                                WinnerDeck.Unit::tier greaterEqual championReq.tier,
                                championReq.itemCount.takeIf { it > 0 }
                                    ?.let { WinnerDeck.Unit::itemNames.pos(it - 1).exists(true) }
                                    ?: null
                            )
                        )
                    }
            )

    private fun buildItemsQuery(itemReqs: List<WinnersReq.ItemReq>): Criteria? =
        if (itemReqs.isEmpty()) null
        else Criteria().andOperator(
            itemReqs
                .map { itemReq ->
                    WinnerDeck::units.elemMatch(
                        WinnerDeck.Unit::itemNames.all(itemReq.dataId)
                    )
                }
        )


    private fun buildSynergiesQuery(synergyReqs: List<WinnersReq.SynergyReq>): Criteria? =
        if (synergyReqs.isEmpty()) null
        else Criteria().andOperator(
            synergyReqs
                .map { synergyReq ->
                    WinnerDeck::traits.elemMatch(
                        and(
                            WinnerDeck.Trait::name eq synergyReq.dataId,
                            WinnerDeck.Trait::tier_current greaterEqual synergyReq.tier,
                        )
                    )
                }
        )


    private fun buildAugmentsBooleanBuilder(augmentReqs: List<WinnersReq.AugmentReq>): Criteria? =
        if (augmentReqs.isEmpty()) null
        else WinnerDeck::augments.all(augmentReqs.map { it.dataId })
}