package com.tft.guide.controller.response

import org.springframework.data.mongodb.core.mapping.Field

data class WinnersResponse(
        val winners: List<Winner>? = null,
) {
    data class Winner(
            val gameVersion: String? = null,
            val goldLeft: Int? = null,
            val lastRound: Int? = null,
            val level: Int? = null,
            val placement: Int? = null,
            val playersEliminated: Int? = null,
            val puuid: String? = null,
            val tftSetNumber: Int? = null,
            val timeEliminated: Float? = null,
            val totalDamageToPlayers: Int? = null,
            val traits: List<Trait>? = null,
            val units: List<Unit>? = null,
    )

    data class Trait(
            val name: String? = null,
            val numUnits: Int? = null,
            val style: Int? = null,
            val tierCurrent: Int? = null,
            val tierTotal: Int? = null,
    )

    data class Unit(
            val items: List<Int>? = null,
            val itemNames: List<String>? = null,
            val characterId: String? = null,
            val name: String? = null,
            val rarity: Int? = null,
            val tier: Int? = null,
    )
}