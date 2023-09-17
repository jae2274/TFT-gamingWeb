package com.tft.guide.repository

import com.jyoliar.mongo.and
import com.jyoliar.mongo.eq
import com.jyoliar.mongo.greaterEqual
import com.tft.guide.controller.request.WinnersReq
import com.tft.guide.entity.BaseDeck
import com.tft.guide.entity.Deck
import org.litote.kmongo.pos
import org.springframework.data.domain.Sort
import org.springframework.data.mapping.toDotPath
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.*
import org.springframework.stereotype.Repository
import org.litote.kmongo.div

@Repository
class DeckQueryRepository(
    val mongoOperations: MongoOperations,
) {
    fun findWinnerDecks(request: WinnersReq): List<Deck> =
        mongoOperations.find(
            Query.query(
                and(
                    (Deck::info / BaseDeck.Info::tft_set_core_name) eq request.season,
                    buildChampionsQuery(request.champions),
                    buildItemsQuery(request.items),
                    buildSynergiesQuery(request.synergies),
                    buildAugmentsBooleanBuilder(request.augments),
                    Deck::placement eq 1,
                )
            )
                .with(Sort.by(Sort.Direction.DESC, (Deck::info / BaseDeck.Info::game_datetime).toDotPath()))
                .skip(request.offset)
                .limit(request.size.toInt()),
            Deck::class.java
        )


    private fun buildChampionsQuery(championReqs: List<WinnersReq.ChampionReq>): Criteria? =
        if (championReqs.isEmpty()) null
        else
            Criteria().andOperator(
                championReqs
                    .map { championReq ->
                        Deck::units.elemMatch(
                            and(
                                BaseDeck.Unit::character_id eq championReq.dataId,
                                BaseDeck.Unit::tier greaterEqual championReq.tier,
                                championReq.itemCount.takeIf { it > 0 }
                                    ?.let { BaseDeck.Unit::itemNames.pos(it - 1).exists(true) }
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
                    Deck::units.elemMatch(
                        BaseDeck.Unit::itemNames.all(itemReq.dataId)
                    )
                }
        )


    private fun buildSynergiesQuery(synergyReqs: List<WinnersReq.SynergyReq>): Criteria? =
        if (synergyReqs.isEmpty()) null
        else Criteria().andOperator(
            synergyReqs
                .map { synergyReq ->
                    Deck::traits.elemMatch(
                        and(
                            BaseDeck.Trait::name eq synergyReq.dataId,
                            BaseDeck.Trait::tier_current greaterEqual synergyReq.tier,
                        )
                    )
                }
        )


    private fun buildAugmentsBooleanBuilder(augmentReqs: List<WinnersReq.AugmentReq>): Criteria? =
        if (augmentReqs.isEmpty()) null
        else Deck::augments.all(augmentReqs.map { it.dataId })
}