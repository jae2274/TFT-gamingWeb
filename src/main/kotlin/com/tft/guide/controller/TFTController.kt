package com.tft.guide.controller

import com.tft.guide.controller.request.WinnersRequest
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

    @GetMapping("/synergies")
    fun synergies(
            @RequestParam season: String
    ): SynergiesRes {
        return tftService.synergies(season)
    }

    @GetMapping("/champions")
    fun champions(
            @RequestParam season: String
    ): ChampionsRes {
        return tftService.champions(season)
    }

    @GetMapping("/items")
    fun items(
            @RequestParam season: String
    ): ItemsRes {
        return tftService.items(season)
    }

    @GetMapping("/augments")
    fun augments(
            @RequestParam season: String
    ): AugmentsRes {
        return tftService.augments(season)
    }

    @PostMapping("/winners")
    fun winners(@RequestBody winnersRequest: WinnersRequest): WinnersRes {
        return tftService.winners(winnersRequest)
    }
}