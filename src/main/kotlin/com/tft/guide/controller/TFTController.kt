package com.tft.guide.controller

import com.tft.guide.controller.request.WinnersRequest
import com.tft.guide.controller.response.ChampionsResponse
import com.tft.guide.controller.response.SynergiesRes
import com.tft.guide.controller.response.WinnersResponse
import com.tft.guide.service.TFTService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TFTController(
        private val tftService: TFTService
) {

    @GetMapping("/synergies")
    fun synergies(): SynergiesRes {
        return tftService.synergies()
    }

    @GetMapping("/champions")
    fun champions(): ChampionsResponse {
        return tftService.champions()
    }

//    @PostMapping("/winners")
//    fun winners(@RequestBody winnersRequest: WinnersRequest): WinnersResponse {
//        return tftService.winners(winnersRequest)
//    }
}