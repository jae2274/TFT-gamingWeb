package com.tft.guide.service

import com.tft.guide.controller.request.WinnersRequest
import com.tft.guide.controller.response.ChampionsResponse
import com.tft.guide.controller.response.SynergiesRes
import com.tft.guide.controller.response.WinnersResponse
import com.tft.guide.entity.Champion
import com.tft.guide.entity.Synergy
import com.tft.guide.repository.ChampionRepository
//import com.tft.guide.repository.QueryRepository
import com.tft.guide.repository.SynergyRepository
import org.springframework.stereotype.Service
import java.util.function.Function
import java.util.stream.Collectors

@Service
class TFTService(

        private val synergyRepository: SynergyRepository,
        private val championRepository: ChampionRepository,
//        private val queryRepository: QueryRepository,
) {
    fun synergies(): SynergiesRes {
        val synergies: List<Synergy> = synergyRepository.findAll()
        return SynergiesRes.of(synergies)
    }

    fun champions(): ChampionsResponse {
        val champions: List<Champion> = championRepository.findAll()
        return ChampionsResponse.of(champions)
    }

//    fun winners(winnersRequest: WinnersRequest): WinnersResponse {
//        val participants: List<Participant> = queryRepository.findByCharacterId(winnersRequest.getUnits().stream().map { unit -> unit.getCharacterId() }.collect(Collectors.toList()))
//        return WinnersResponse.builder()
//                .winners(
//                        participants.stream().map(
//                                Function<Participant, Any> { participant: Participant? -> MatchMapper.INSTANCE.entryToDTO(participant) }
//                        ).collect(Collectors.toList())
//                )
//                .build()
//    }
}