package com.tft.guide.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "winnerDeck")
data class WinnerDeck(
    @Id
    var _id: String? = null,
    var match_id: String = "",
    var info: Info = Info(),
    var gold_left: Int? = null,
    var last_round: Int? = null,
    var level: Int? = null,
    var traits: List<Trait> = emptyList(),
    var units: List<Unit> = emptyList(),
    var augments: List<String> = emptyList(),
    var createdAt: LocalDateTime,
) {

    data class Info(
        var game_datetime: Long? = null,
        var game_length: Float? = null,
        var queue_id: Int? = null,
        var game_version: String = "",
        var tft_game_type: GameType? = null,
        var tft_set_core_name: String? = null,
        var tft_set_number: Int? = null,
    )

    data class Trait(
        var name: String? = null,
        var num_units: Int? = null,
        var style: Int? = null,
        var tier_current: Int? = null,
        var tier_total: Int? = null,
    )

    data class Unit(
        var items: List<Int>? = null,
        var itemNames: List<String>? = null,
        var character_id: String = "",
        var chosen: String? = null,
        var name: String? = null,
        var rarity: Int? = null,
        var tier: Int? = null,
    )
}

