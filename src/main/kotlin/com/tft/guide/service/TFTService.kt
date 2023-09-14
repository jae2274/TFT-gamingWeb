package com.tft.guide.service

import com.tft.guide.controller.request.MatchRequest
import com.tft.guide.controller.request.WinnersReq
import com.tft.guide.controller.response.WinnersRes
import com.tft.guide.controller.response.*
import com.tft.guide.entity.*
import com.tft.guide.entity.utils.add
import com.tft.guide.entity.utils.listOf
import com.tft.guide.entity.utils.union
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
    private val deckRepository: DeckRepository,
    private val idSetRepository: IdSetRepository,
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
        val winnerDecks: List<Deck> = deckQueryRepository.findWinnerDecks(winnersRequest)
        return WinnersRes.of(winnerDecks)
    }

    fun items(season: String): ItemsRes {
        val items: List<Item> = itemRepository.findAllBySeason(season)
        return ItemsRes.of(items)
    }

    fun stats(season: String): TftStatsRes {
        val tftStats = tftStatsRepository.findTopBySeasonNumberOrderByCreatedAtDesc(season.toInt())
        return TftStatsRes.of(tftStats)
    }

    fun augments(season: String): AugmentsRes {
        val augments = augmentRepository.findAllBySeason(season)
        return AugmentsRes.of(augments)
    }

//    fun saveMatches(matches: List<MatchRequest>) {
//        val decks = matches.map { Deck.listOf(it) }.flatten()
////        val decks = Deck.listOf(matchDTO)
//        val idSets = IdSet.listOf(decks)
//        val tftStatsList = TftStats.listOf(decks)
//
//        saveDecks(decks)
//        saveIdSets(idSets)
//        saveTftStats(tftStatsList)
//    }

    fun saveMatches(matches: List<MatchRequest>) {
        val filteredMatches = matches.filter { !deckRepository.existsByMatchId(it.metadata.match_id) }

        val decks = filteredMatches.map { Deck.listOf(it) }.flatten()
        val idSets = IdSet.listOf(decks)
        val tftStatsList = TftStats.listOf(decks)

        deckRepository.saveAll(decks)
        saveIdSets(idSets)
        saveTftStats(tftStatsList)
    }

    private fun saveIdSets(idSets: Collection<IdSet>) {
        idSets
            .map { newIdSet ->
                idSetRepository.findBySeasonAndType(newIdSet.season, newIdSet.seasonNumber, newIdSet.type)
                    ?.let { alreadyIdSet -> alreadyIdSet.union(newIdSet) }
                    ?: newIdSet
            }
            .let { idSetRepository.saveAll(it) }
    }

    private fun saveTftStats(tftStatsList: List<TftStats>) {
        tftStatsList
            .map { newTftStats ->
                tftStatsRepository.findByGameVersion(newTftStats.gameVersion)
                    ?.also { alreadyTftStats -> alreadyTftStats.add(newTftStats) }
                    ?: newTftStats
            }
            .let { tftStatsRepository.saveAll(it) }
    }
}