package com.tft.guide.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime


@Document(collection = "winnerDeck")
data class WinnerDeck(
    @Id
    val _id: String,
    @field:Indexed(unique = true)
    val match_id: String,
    val info: Info,
    val gold_left: Int,
    val last_round: Int,
    val level: Int,
    val traits: List<Trait>,
    val units: List<Unit>,
    val augments: List<String>,
    val createdAt: LocalDateTime = LocalDateTime.now()
) : BaseDeck() {
    companion object {

        fun of(deck: Deck): WinnerDeck {
            return WinnerDeck(
                _id = deck._id!!,
                match_id = deck.match_id,
                info = deck.info,
                gold_left = deck.gold_left,
                last_round = deck.last_round,
                level = deck.level,
                traits = deck.traits,
                units = deck.units,
                augments = deck.augments,
            )
        }
    }
}

