package com.tft.guide.entity

import com.tft.guide.controller.request.MatchRequest
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime


@Document(collection = "deck")
@CompoundIndex(def = "{'match_id': 1, 'placement': 1}", unique = true)
data class Deck(
    @Id
    val _id: String? = null,
    val match_id: String,
    val info: Info,
    val gold_left: Int,
    val last_round: Int,
    val level: Int,
    val placement: Int,
    val traits: List<Trait>,
    val units: List<Unit>,
    val augments: List<String>,
    val createdAt: LocalDateTime = LocalDateTime.now()
) : BaseDeck() {
    companion object {
        fun listOf(matchDTO: MatchRequest): List<Deck> {
            return matchDTO.info.participants
                .map { participant -> of(matchDTO.metadata.match_id, matchDTO.info, participant) }
        }

        fun of(match_id: String, info: MatchRequest.InfoDTO, participant: MatchRequest.ParticipantDTO): Deck {
            return Deck(
                match_id = match_id,
                info = Info.of(info),
                gold_left = participant.gold_left,
                last_round = participant.last_round,
                level = participant.level,
                placement = participant.placement,
                traits = Trait.listOf(participant.traits),
                units = Unit.listOf(participant.units),
                augments = participant.augments,
            )
        }


    }
}

fun Collection<Deck>.checkAllSameSeason() {
    this.map { it.info.tft_set_core_name }.distinct()
        .also { check(it.size == 1) { "deck contains multiple season $it" } }
    this.map { it.info.tft_set_number }.distinct()
        .also { check(it.size == 1) { "deck contains multiple seasonNumber $it" } }
}

fun Collection<Deck>.checkValidSeason(season: String, seasonNumber: Int) {
    checkAllSameSeason()
    this.map { it.info.tft_set_core_name }
        .also { check(it[0] == season) { "different season: ${it[0]}, $season" } }

    this.map { it.info.tft_set_number }
        .also { check(it[0] == seasonNumber) { "different seasonNumber: ${it[0]}, $season" } }
}

fun Collection<Deck>.checkAllSameVersion() {
    this.map { it.info.game_version }.distinct()
        .also { check(it.size == 1) { "deck contains multiple version $it" } }
}

fun Collection<Deck>.checkValidGameVersion(gameVersion: String) {
    checkAllSameVersion()
    this.map { it.info.game_version }
        .also { check(it[0] == gameVersion) { "different version:  ${it[0]}, $gameVersion" } }
}


