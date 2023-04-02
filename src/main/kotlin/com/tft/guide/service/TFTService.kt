package com.tft.guide.service

import com.tft.guide.controller.request.WinnersReq
import com.tft.guide.controller.response.*
import com.tft.guide.entity.Champion
import com.tft.guide.entity.WinnerDeck
import com.tft.guide.entity.Item
import com.tft.guide.entity.Synergy
import com.tft.guide.repository.*
//import com.tft.guide.repository.QueryRepository
import org.springframework.stereotype.Service

@Service
class TFTService(

        private val synergyRepository: SynergyRepository,
        private val championRepository: ChampionRepository,
        private val deckQueryRepository: DeckQueryRepository,
        private val itemRepository: ItemRepository,
        private val augmentRepository: AugmentRepository,
        private val tftStatsRepository: TFTStatsRepository,
//        private val queryRepository: QueryRepository,
) {
    fun synergies(season: String): SynergiesRes {
        val synergies: List<Synergy> = synergyRepository.findAllBySeason(season)
        return SynergiesRes.of(synergies)
    }

    fun champions(season: String): ChampionsRes {
        val champions: List<Champion> = championRepository.findAllBySeason(season)
        return ChampionsRes.of(champions)
    }

    fun winners(winnersRequest: WinnersReq): WinnersRes {
        val winnerDecks: List<WinnerDeck> = deckQueryRepository.findWinnerDecks(winnersRequest)
        return WinnersRes.of(winnerDecks)
    }

    fun items(season: String): ItemsRes {
        val items: List<Item> = itemRepository.findAllBySeason(season)
        return ItemsRes.of(items)
    }

    fun stats(season: String): TftStatsRes {
        val tftStats = tftStatsRepository.findTopByOrderByGameVersionDesc()
        return TftStatsRes.of(tftStats)
    }

    fun augments(season: String): AugmentsRes {
        val augments = augmentRepository.findAllBySeason(season)
        return AugmentsRes.of(augments)
    }
}