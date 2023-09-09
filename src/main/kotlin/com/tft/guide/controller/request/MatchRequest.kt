package com.tft.guide.controller.request


data class MatchesRequest(
    val matches: List<MatchRequest>,
)

data class MatchRequest(
    val metadata: MetadataDTO,
    val info: InfoDTO,
) {
    data class MetadataDTO(
        var data_version: String?,
        var match_id: String,
        var participants: List<String>,
    )

    data class InfoDTO(
        var game_datetime: Long?,
        var game_length: Float?,
        var game_version: String,
        var participants: List<ParticipantDTO>,
        var queue_id: Int?,
        val tft_game_type: String,
        val tft_set_core_name: String,
        val tft_set_number: Int,
    )

    data class ParticipantDTO(
        var gold_left: Int,
        var last_round: Int,
        var level: Int,
        var placement: Int,
        var puuid: String?,
        var traits: List<TraitDTO>,
        var units: List<UnitDTO>,
        var augments: List<String>
    )

    data class TraitDTO(
        var name: String,
        var num_units: Int,
        var style: Int,
        var tier_current: Int,
        var tier_total: Int,
    )

    data class UnitDTO(
        var items: List<Int>?,
        var itemNames: List<String>?,
        var character_id: String,
        var chosen: String?,
        var name: String?,
        var rarity: Int,
        var tier: Int,
    )
}