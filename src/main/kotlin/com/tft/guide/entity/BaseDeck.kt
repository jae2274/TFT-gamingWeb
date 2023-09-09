package com.tft.guide.entity

import com.tft.guide.controller.request.MatchRequest


abstract class BaseDeck {


    data class Info(
        var game_datetime: Long? = null,
        var game_length: Float? = null,
        var queue_id: Int? = null,
        var game_version: String,
        var tft_game_type: String? = null,
        var tft_set_core_name: String = "",
        var tft_set_number: Int = 0,
    ) {
        companion object {
            fun of(infoDTO: MatchRequest.InfoDTO): Info {
                return Info(
                    game_datetime = infoDTO.game_datetime,
                    game_length = infoDTO.game_length,
                    queue_id = infoDTO.queue_id,
                    game_version = infoDTO.game_version,
                    tft_game_type = infoDTO.tft_game_type,
                    tft_set_core_name = infoDTO.tft_set_core_name,
                    tft_set_number = infoDTO.tft_set_number,
                )
            }
        }
    }

    data class Trait(
        var name: String = "",
        var num_units: Int?,
        var style: Int?,
        var tier_current: Int,
        var tier_total: Int?,
    ) {
        companion object {
            fun listOf(traits: List<MatchRequest.TraitDTO>): List<Trait> {
                return traits.map { of(it) }
            }

            private fun of(trait: MatchRequest.TraitDTO): Trait {
                return Trait(
                    name = trait.name,
                    num_units = trait.num_units,
                    style = trait.style,
                    tier_current = trait.tier_current,
                    tier_total = trait.tier_total,
                )
            }
        }
    }

    data class Unit(
        var items: List<Int>?,
        var itemNames: List<String> = listOf(),
        var character_id: String = "",
        var chosen: String?,
        var name: String?,
        var rarity: Int?,
        var tier: Int,
    ) {
        companion object {
            fun listOf(units: List<MatchRequest.UnitDTO>): List<Unit> {
                return units.map { of(it) }
            }

            private fun of(unit: MatchRequest.UnitDTO): Unit {
                return Unit(
                    items = unit.items,
                    itemNames = unit.itemNames ?: emptyList(),
                    character_id = unit.character_id,
                    chosen = unit.chosen,
                    name = unit.name,
                    rarity = unit.rarity,
                    tier = unit.tier,
                )
            }
        }
    }
}


