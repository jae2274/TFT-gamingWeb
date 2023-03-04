package com.tft.guide.controller.response

import com.tft.guide.entity.Deck

data class WinnersRes(
        val winners: List<WinnerRes>? = null,
) {
    data class WinnerRes(
            val gameVersion: String? = null,
            val match_id: String,
            val goldLeft: Int? = null,
            val lastRound: Int? = null,
            val level: Int? = null,
            val placement: Int? = null,
            val playersEliminated: Int? = null,
            val puuid: String? = null,
            val tftSetNumber: Int? = null,
            val timeEliminated: Float? = null,
            val totalDamageToPlayers: Int? = null,
            val traits: List<TraitRes>,
            val units: List<Unit>,
            var augments: List<String>,
    )

    data class TraitRes(
            val name: String? = null,
            val numUnits: Int? = null,
            val style: Int? = null,
            val tierCurrent: Int? = null,
            val tierTotal: Int? = null,

            ) {
        companion object {
            fun listOf(traits: List<Deck.Trait>): List<WinnersRes.TraitRes> {
                return traits.map {
                    TraitRes(
                            name = it.name,
                            numUnits = it.num_units,
                            style = it.style,
                            tierCurrent = it.tier_current,
                            tierTotal = it.tier_total,
                    )
                }
            }
        }
    }

    data class Unit(
            val items: List<Int>,
            val itemNames: List<String>,
            val characterId: String,
            val name: String?,
            val rarity: Int?,
            val tier: Int?,
    ) {
        companion object {
            fun listOf(units: List<Deck.Unit>): List<WinnersRes.Unit> {
                return units.map {
                    Unit(
                            items = it.items ?: emptyList(),
                            itemNames = it.itemNames ?: emptyList(),
                            characterId = it.character_id,
                            name = it.name,
                            rarity = it.rarity,
                            tier = it.tier,
                    )
                }
            }
        }
    }

    companion object {
        fun of(decks: List<Deck>): WinnersRes {
            return WinnersRes(
                    decks
                            .map {
                                WinnerRes(
                                        match_id = it.match_id,
                                        lastRound = it.last_round,
                                        level = it.level,
                                        placement = it.placement,
//                                                playersEliminated = it.info.
//                                                puuid =
                                        tftSetNumber = it.info.tft_set_number,
//                                        timeEliminated =
//                                        totalDamageToPlayers = it.traits =
                                        traits = TraitRes.listOf(it.traits),
                                        units = Unit.listOf(it.units),
                                        augments = it.augments
                                )
                            }
            )
        }
    }
}