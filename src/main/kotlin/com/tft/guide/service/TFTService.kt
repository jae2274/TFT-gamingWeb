package com.tft.guide.service

import com.tft.guide.controller.request.WinnersRequest
import com.tft.guide.controller.response.*
import com.tft.guide.entity.Champion
import com.tft.guide.entity.Deck
import com.tft.guide.entity.Item
import com.tft.guide.entity.Synergy
import com.tft.guide.repository.*
//import com.tft.guide.repository.QueryRepository
import org.springframework.stereotype.Service
import java.util.function.Function
import java.util.stream.Collectors

@Service
class TFTService(

        private val synergyRepository: SynergyRepository,
        private val championRepository: ChampionRepository,
        private val deckRepository: DeckRepository,
        private val itemRepository: ItemRepository,
        private val augmentRepository: AugmentRepository,
//        private val queryRepository: QueryRepository,
) {
    fun synergies(season: String): SynergiesRes {
        val synergies: List<Synergy> = synergyRepository.findAllBySeason(season)
        return SynergiesRes.of(synergies)
    }

    fun champions(season: String): ChampionsResponse {
        val champions: List<Champion> = championRepository.findAllBySeason(season)
        return ChampionsResponse.of(champions)
    }

    fun winners(winnersRequest: WinnersRequest): WinnersResponse {
        val decks: List<Deck> = deckRepository.findByCharacterId(winnersRequest.units.map { unit -> unit.characterId })
        return WinnersResponse.of(decks)
    }

    fun items(season: String): ItemsRes {
        val items: List<Item> = itemRepository.findAllBySeason(season)
        return ItemsRes.of(items)
    }

    fun augments(season: String): AugmentsRes {
        val augments = augmentRepository.findAllBySeason(season)
        return AugmentsRes.of(augments)
    }
}