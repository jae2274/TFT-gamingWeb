package com.tft.guide.controller.response

import com.tft.guide.entity.BaseDeck
import com.tft.guide.entity.WinnerDeck

data class WinnersRes(
    val winners: List<WinnerRes>?,
) {
    data class WinnerRes(
        val gameVersion: String?,
        val match_id: String,
        val lastRound: Int?,
        val level: Int?,
        val tftSetNumber: Int?,
        val traits: List<TraitRes>,
        val units: List<Unit>,
        var augments: List<String>,
    )

    data class TraitRes(
        val name: String?,
        val numUnits: Int?,
        val style: Int?,
        val tierCurrent: Int?,
        val tierTotal: Int?,

        ) {
        companion object {
            fun listOf(traits: List<BaseDeck.Trait>): List<TraitRes> {
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
            fun listOf(units: List<BaseDeck.Unit>): List<Unit> {
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
        fun of(winnerDecks: List<WinnerDeck>): WinnersRes {
            return WinnersRes(
                winnerDecks
                    .map {
                        WinnerRes(
                            gameVersion = it.info.game_version,
                            match_id = it.match_id,
                            lastRound = it.last_round,
                            level = it.level,
                            tftSetNumber = it.info.tft_set_number,
                            traits = TraitRes.listOf(it.traits),
                            units = Unit.listOf(it.units),
                            augments = it.augments,
                        )
                    }
            )
        }
    }
}