package com.tft.guide.controller.request

data class WinnersRequest(
        val units: List<Unit>
) {
    data class Unit(
            val characterId: String
    )
}