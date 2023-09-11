package com.tft.guide.controller

import com.tft.guide.CommonResponse
import com.tft.guide.controller.request.*
import com.tft.guide.controller.response.*
import com.tft.guide.service.TFTService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class TFTController(
    private val tftService: TFTService
) {
    object Url {
        const val GET_SYNERGIES: String = "/synergies"
        const val GET_CHAMPIONS: String = "/champions"
        const val GET_ITEMS: String = "/items"
        const val GET_AUGMENTS: String = "/augments"
        const val GET_STATS: String = "/stats"
        const val POST_WINNERS: String = "/winners"
        const val POST_MATCHES: String = "/matches"
    }

    object QueryParam {
        const val SEASON: String = "season"
    }

    @GetMapping(Url.GET_SYNERGIES)
    fun synergies(
        @RequestParam(QueryParam.SEASON) season: String
    ): CommonResponse<SynergiesRes> {
        return tftService.synergies(season)
            .let { CommonResponse.successOf(it) }
    }

    @GetMapping(Url.GET_CHAMPIONS)
    fun champions(
        @RequestParam(QueryParam.SEASON) season: String
    ): CommonResponse<ChampionsRes> {
        return tftService.champions(season)
            .let { CommonResponse.successOf(it) }
    }

    @GetMapping(Url.GET_ITEMS)
    fun items(
        @RequestParam(QueryParam.SEASON) season: String
    ): CommonResponse<ItemsRes> {
        return tftService.items(season)
            .let { CommonResponse.successOf(it) }
    }

    @GetMapping(Url.GET_AUGMENTS)
    fun augments(
        @RequestParam(QueryParam.SEASON) season: String
    ): CommonResponse<AugmentsRes> {
        return tftService.augments(season)
            .let { CommonResponse.successOf(it) }
    }

    @GetMapping(Url.GET_STATS)
    fun stats(
        @RequestParam(QueryParam.SEASON) season: String
    ): CommonResponse<TftStatsRes> {
        return tftService.stats(season)
            .let { CommonResponse.successOf(it) }
    }

    @PostMapping(Url.POST_WINNERS)
    fun winners(@RequestBody winnersRequest: WinnersReq): CommonResponse<WinnersRes> {
        return tftService.winners(winnersRequest)
            .let { CommonResponse.successOf(it) }
    }

    @PostMapping(Url.POST_MATCHES)
    fun saveMatches(@RequestBody matchReq: MatchRequest): CommonResponse<Unit> {
        return tftService.saveMatch(matchReq)
            .let { CommonResponse.successOf(it) }
    }
}